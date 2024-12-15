---
icon: filter
article: false
---

# 过滤器

## 作用

参考 [codec-server#XtreamFilter](/guide/server/request-processing/filter.md)

## 示例

要自定义 过滤器，只需要将 `XtreamFilter` 类型的 **Bean** 加入到 **spring** 容器中即可。

```java
@Component
public class LoggingXtreamFilter implements XtreamFilter {

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
