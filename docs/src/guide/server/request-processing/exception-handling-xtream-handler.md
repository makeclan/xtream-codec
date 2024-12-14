---
icon: arrows-spin
---

# ExceptionHandlingXtreamHandler

## 介绍

::: tip

`ExceptionHandlingXtreamHandler` 借鉴于 `org.springframework.web.server.handler.ExceptionHandlingWebHandler`。

:::

从名称可以看出来，`ExceptionHandlingXtreamHandler` 本质上是一个 `XtreamHandler`，前缀 `ExceptionHandling` 表示该组件具有处理异常的功能。

`ExceptionHandlingXtreamHandler` 内部维护着一堆 `XtreamRequestExceptionHandler` 和一个 `XtreamHandler`(实际上是`FilteringXtreamHandler`)。

![](/img/server/request-processing/exception-handling-xtream-handler.png)

实现比较直观，不再做过多的介绍。下面是源码：

```java
public class ExceptionHandlingXtreamHandler implements XtreamHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingXtreamHandler.class);
    private final XtreamHandler delegateHandler;
    private final List<XtreamRequestExceptionHandler> exceptionHandlers;

    public ExceptionHandlingXtreamHandler(XtreamHandler delegateHandler, List<XtreamRequestExceptionHandler> exceptionHandlers) {
        this.delegateHandler = delegateHandler;

        final List<XtreamRequestExceptionHandler> list = new ArrayList<>();
        list.add(new XtreamRequestExceptionHandler.CheckpointInsertingXtreamRequestExceptionHandler());
        list.addAll(exceptionHandlers);
        this.exceptionHandlers = OrderedComponent.sort(list);
    }

    @Override
    public Mono<Void> handle(XtreamExchange exchange) {
        Mono<Void> completion;
        try {
            completion = this.delegateHandler.handle(exchange);
        } catch (Throwable ex) {
            completion = Mono.error(ex);
        }

        for (XtreamRequestExceptionHandler handler : this.exceptionHandlers) {
            completion = completion.onErrorResume(ex -> {
                final Throwable cause = XtreamWrappedRuntimeException.unwrapIfNecessary(ex);
                return handler.handleRequestException(exchange, cause);
            });
        }

        return completion;
    }
}
```
