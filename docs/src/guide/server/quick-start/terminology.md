---
icon: info
---

# 基本术语

## XtreamRequest

::: tip

`XtreamRequest` 借鉴于 `org.springframework.http.server.reactive.ServerHttpRequest`

:::

- 客户端报文中的字节流最终会解析到 `XtreamRequest` 里。
- 同时会将本次请求的其他元数据封装到 `XtreamRequest` 中， 例如：对方 IP 地址、协议类型等。
- `XtreamRequest` 是不可变对象( **Immutable**)。一旦创建便不可修改。
- 通过 `request.mutate()` 方法可以从当前实例构建出一个新实例的 `Builder`。

`XtreamRequest` 接口内容如下：

```java
public interface XtreamRequest extends XtreamInbound {
    enum Type {
        TCP,
        UDP
    }

    Type type();

    ByteBufAllocator bufferFactory();

    NettyInbound underlyingInbound();

    Channel underlyingChannel();

    /**
     * 同一个 ”网络包“ 的请求和响应，该值应该确保一致（即使是在中途重新包装或修改了 {@link XtreamRequest} 对象）。
     */
    String requestId();

    InetSocketAddress remoteAddress();

    ByteBuf payload();

    Map<String, Object> attributes();

    default void release() {
        XtreamBytes.releaseBuf(this.payload());
    }

    XtreamRequestBuilder mutate();

    @SuppressWarnings("unchecked")
    default <T extends XtreamRequest> T castAs(Class<T> ignored) {
        return (T) this;
    }
}
```

## XtreamResponse

::: tip

`XtreamResponse` 借鉴于 `org.springframework.http.server.reactive.ServerHttpResponse`

:::

有 `Request` 就有 `response`。`XtreamResponse` 是和 `XtreamRequest` 相对应的。也就是说 `XtreamResponse` 不会单独存在，一定是和某个 `XtreamRequest` 对应的。

通过 `XtreamResponse` 可以下发数据给客户端。

```java
public interface XtreamResponse extends XtreamOutbound {
    // 其实这几个方法都是在 XtreamOutbound 中定义的
    ByteBufAllocator bufferFactory();

    XtreamRequest.Type type();

    NettyOutbound outbound();

    InetSocketAddress remoteAddress();

    Mono<Void> writeWith(Publisher<? extends ByteBuf> body);

    Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends ByteBuf>> publisher);

}
```

## XtreamSession

::: tip

`XtreamSession` 借鉴于 `org.springframework.web.server.WebSession`

:::

`XtreamSession` 是对当前客户端的会话的封装，主要有如下作用：

- 和 `XtreamResponse` 一样，`XtreamSession` 也有下发数据的能力(`XtreamOutbound`)
- `session.invalidate()` 方法可以关闭会话(断开连接)，进而触发对 `XtreamSessionEventListener` 的回调
- 通过 `session.attributes()` 方法可以保存和当前回话相关的元数据

```java
public interface XtreamSession extends XtreamOutbound {

    String id();

    Map<String, Object> attributes();

    Instant creationTime();

    /**
     * @return 上次通信时间
     */
    Instant lastCommunicateTime();

    XtreamSession lastCommunicateTime(Instant current);

    void invalidate(XtreamSessionEventListener.SessionCloseReason reason);
}
```

## XtreamExchange

::: tip

`XtreamExchange` 借鉴于 `org.springframework.web.server.ServerWebExchange`

:::

- `XtreamExchange` 聚合了`XtreamRequest` 、`XtreamResponse` 和 `XtreamSession`。
- `XtreamExchange` 是不可变对象( **Immutable**)。一旦创建便不可修改。
- 通过 `exchange.mutate()` 方法可以从当前实例构建出一个新实例的 `Builder`。

```java
public interface XtreamExchange {
    default ByteBufAllocator bufferFactory() {
        return response().bufferFactory();
    }

    XtreamRequest request();

    XtreamResponse response();

    Mono<XtreamSession> session();

    default XtreamExchangeBuilder mutate() {
        return new DefaultXtreamExchangeBuilder(this);
    }

    default Map<String, Object> attributes() {
        return request().attributes();
    }
}
```
