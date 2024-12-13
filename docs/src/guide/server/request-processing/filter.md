---
date: 2024-12-10
icon: info
---

# XtreamFilter

## 介绍

::: tip

`XtreamFilter` 借鉴于 `org.springframework.web.server.WebFilter`。

:::

`XtreamFilter` 类似于 **WebFlux** 中的 `org.springframework.web.server.WebFilter` 和 **Servlet** 中的 `javax.servlet.Filter`。 在处理器之前执行。

其接口定义如下：

```java
public interface XtreamFilter extends OrderedComponent {
    Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain);
}
```

可以使用 `XtreamFilter` 实现以下功能：

- 鉴权
- 日志记录
- 切换线程池
- 修改请求内容
- 转发请求到其他服务
- 加解密
- 直接写数据给客户端(终止后续的处理流程)
- ……

## 示例

下面是一个使用 `XtreamFilter` 的例子，用于合并 **JT/T 808** 的子包。

```java
public class Jt808RequestCombinerFilter implements XtreamFilter {
    public static final int ORDER = -100;
    protected final Jt808RequestCombiner requestCombiner;
    protected final Jt808RequestLifecycleListener lifecycleListener;

    public Jt808RequestCombinerFilter(Jt808RequestCombiner requestCombiner, Jt808RequestLifecycleListener lifecycleListener) {
        this.requestCombiner = requestCombiner;
        this.lifecycleListener = lifecycleListener;
    }

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        final Jt808Request jt808Request = (Jt808Request) exchange.request();

        // 不是子包
        if (!jt808Request.header().messageBodyProps().hasSubPackage()) {
            return chain.filter(exchange);
        }

        // 合并子包
        final Jt808Request mergedRequest = this.requestCombiner.tryMergeSubPackage(jt808Request);
        // 如果还有子包没到达，终止后续流程，直至所有子包都到达
        if (mergedRequest == null) {
            return Mono.empty();
        }

        // 所有子包都已经到达，继续执行后续流程
        final XtreamExchange mutatedExchange = this.mutatedExchange(exchange, mergedRequest);

        this.lifecycleListener.afterSubPackageMerged(mutatedExchange, mergedRequest);

        return chain.filter(mutatedExchange).doFinally(signalType -> {
            // ...
            mergedRequest.release();
        });
    }

    protected XtreamExchange mutatedExchange(XtreamExchange exchange, Jt808Request request) {
        return exchange.mutate().request(request).build();
    }

    @Override
    public int order() {
        return ORDER;
    }
}
```
