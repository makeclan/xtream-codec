---
icon: info
---

# XtreamRequestExceptionHandler

## 介绍

::: tip

- `XtreamRequestExceptionHandler` 借鉴于 `org.springframework.web.server.WebExceptionHandler`
- 该组件实现了 `OrderedComponent` 接口，可以有多个

:::

`XtreamRequestExceptionHandler` 是用来处理请求处理过程中可能出现的异常的组件。

接口定义如下：

```java
public interface XtreamRequestExceptionHandler extends OrderedComponent {
    Mono<Void> handleRequestException(XtreamExchange exchange, Throwable ex);
}
```

- 如果当前异常能处理，内部处理掉异常并返回 `Mono.empty()`，表示处理完成。
- 反之，则返回 `Mono.error(ex)`，将异常传递下去。

## 示例

```java
public class LoggingXtreamRequestExceptionHandler implements XtreamRequestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(LoggingXtreamRequestExceptionHandler.class);

    @Override
    public Mono<Void> handleRequestException(XtreamExchange exchange, Throwable ex) {
        if (ex instanceof RequestHandlerNotFoundException notFoundException) {
            log.error("[LoggingXtreamRequestExceptionHandler] RequestHandlerNotFoundException: {}", notFoundException.getExchange());
            // 异常被处理 不再往下传递
            return Mono.empty();
        }
        // 继续抛出异常
        return Mono.error(ex);
    }

    @Override
    public int order() {
        return OrderedComponent.LOWEST_PRECEDENCE - 1;
    }
}
```
