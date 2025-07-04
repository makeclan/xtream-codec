---
icon: filter
article: false
---

# 过滤器

## 作用

参考 [codec-server#XtreamFilter](/guide/server/request-processing/filter.md)

## 示例

要自定义 过滤器，只需要将 `Jt808RequestFilter` 类型的 **Bean** 加入到 **spring** 容器中即可。

```java {3}

@Component
public class LoggingXtreamFilter implements Jt808RequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingXtreamFilter.class);

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        log.info("==> Receive [{}] message: requestId = {}, remoteAddr = {}, payload = {}",
                exchange.request().type(),
                exchange.request().requestId(),
                exchange.request().remoteAddress(),
                FormatUtils.toHexString(exchange.request().payload())
        );
        return chain.filter(exchange);
    }

    @Override
    public int order() {
        return OrderedComponent.HIGHEST_PRECEDENCE + 1000;
    }
}
```

如果只想让自定义 `filter` 对 **TCP** 请求有效，同时实现 `TcpXtreamFilter` 即可(**UDP** 也是类似的使用方式)，示例如下：

```java {3}

@Component
public class LoggingXtreamFilter implements Jt808RequestFilter, TcpXtreamFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingXtreamFilter.class);

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        log.info("==> Receive [{}] message: requestId = {}, remoteAddr = {}, payload = {}",
                exchange.request().type(),
                exchange.request().requestId(),
                exchange.request().remoteAddress(),
                FormatUtils.toHexString(exchange.request().payload())
        );
        return chain.filter(exchange);
    }

    @Override
    public int order() {
        return OrderedComponent.HIGHEST_PRECEDENCE + 1000;
    }
}
```
