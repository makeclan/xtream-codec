---
date: 2024-12-10
icon: cube
---

# NettyHandlerAdapter

## 请求处理流程

下图是一个简化版的请求处理流程：

![](/img/server/request-processing/request-flow-0.png)

通过上图不难看出，`NettyHandlerAdapter` 是 **xtream** 中客户端请求的入口。
也就是说，从这里开始，**Xtream** 接管了 `reactor-netty` 收到的客户端数据。

## 和 reactor-netty 的整合

下图是 **reactor-netty** 官方文档中 [Consuming Data](https://projectreactor.io/docs/netty/release/reference/tcp-server.html#consuming-data) 章节的截图：

![](/img/server/request-processing/reactor-netty-consuming-data.png)

`NettyHandlerAdapter` 的角色就是上图中标红的 `handle()` 方法的参数。

`handle()` 方法参数类型是 `BiFunction<? super NettyInbound, ? super NettyOutbound, ? extends Publisher<Void>>`。

```java
public abstract class TcpServer extends ServerTransport<TcpServer, TcpServerConfig> {
	public TcpServer handle(BiFunction<? super NettyInbound, ? super NettyOutbound, ? extends Publisher<Void>> handler) {
		Objects.requireNonNull(handler, "handler");
		return doOnConnection(new OnConnectionHandle(handler));
	}
}
```

## 内置实现

- `DefaultTcpXtreamNettyHandlerAdapter`: 处理 **TCP** 请求的内置实现
- `DefaultUdpXtreamNettyHandlerAdapter`: 处理 **UDP** 请求的内置实现

这两个内置实现大同小异，基本流程如下:

- 将请求信息封装为统一的 `XtreamExchange`
    - `XtreamRequest`
    - `XtreamResponse`
    - `XtreamSession`
- 将 `XtreamExchange` 的处理委托给`XtreamHandler.handle(XtreamExchange)`
- 从这里开始，统一了 **TCP** 和 **UDP** 的编程接口

有关 `XtreamHandler` 的更多信息，见 [XtreamHandler](./xtream-handler.md) 。
