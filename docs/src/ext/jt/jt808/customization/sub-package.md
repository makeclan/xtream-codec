---
icon: diagram-project
article: false
---

# 消息分包

## 请求分包

内置了一个 `Jt808RequestCombinerFilter` 用来将分包请求合并为一个完整的请求。内部依赖 `Jt808RequestCombiner` 进行临时子包存储。

如果内置子包合并逻辑不符合要求，可以自行实现 `Jt808RequestCombinerFilter` 或 `Jt808RequestCombiner`。

```java
public class Jt808RequestCombinerFilter implements Jt808RequestFilter {
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

## 响应分包

响应消息的分包逻辑在 `DefaultJt808ResponseEncoder` 中实现；如果内置分包逻辑不符合要求，可以改造 `DefaultJt808ResponseEncoder`。
