---
icon: repeat
article: false
---

# XtreamHandler

## 作用

上一章节的 [NettyHandlerAdapter](./netty-handler-adapter.md) 中，将 TCP 和 UDP 请求封装成了统一的 `XtreamExchange`。

本章节的 `XtreamHandler` 就是用来处理 `XtreamExchange` 的。

![](/img/server/request-processing/request-flow-1.png)

接口定义如下：

```java
public interface XtreamHandler {
    Mono<Void> handle(XtreamExchange exchange);
}
```

通过参数中的 `XtreamExchange`，可以方便地处理 **TCP/UDP** 请求：

- `XtreamRequest`: 本次请求
- `XtreamResponse`: 和 `XtreamRequest` 相对应的响应
- `XtreamSession`: 对应的客户端会话信息

## 内置实现

有如下三个内置实现，三个内置实现层层递进，后者是对前者的装饰：

- 1). `DispatcherXtreamHandler`: 为了方便扩展，这个内置实现聚合了下面组件:
    - `XtreamHandlerMapping`: 确定当前请求可以被那个处理器处理，返回处理器
    - `XtreamHandlerAdapter`: 对上一步返回的处理器的适配器，因为实际场景中处理器可能是各种各样的，所以需要适配器
    - `XtreamHandlerResultHandler`: 对适配器的执行结果(实际上是处理器的返回值)的再次处理(统一编码、加密等)
- 2). `FilteringXtreamHandler`
    - 这个内置实现聚合了 `DispatcherXtreamHandler` 和 `XtreamFilter`
    - 在 `DispatcherXtreamHandler` 执行之前，会有一堆 `XtreamFilter` 的逻辑。
- 3). `ExceptionHandlingXtreamHandler`
    - 这个内置实现聚合了 `FilteringXtreamHandler` 和 `XtreamRequestExceptionHandler`
    - 在 `FilteringXtreamHandler` 执行前，先绑定好 `reactor` 的异常处理器；这样一来，在后续流程异常时可以通过 `XtreamRequestExceptionHandler` 来处理异常

下图是三个内置实现之间的依赖关系：

![](/img/server/request-processing/exception-handling-xtream-handler.png)
