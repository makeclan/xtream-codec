---
date: 2024-12-10
icon: info
---

# 消息下发

一般来说，至少有三种方式发送数据给客户端：

1. 通过 [XtreamResponse](#xtreamresponse)
2. 通过 [XtreamSession](#xtreamsession)
3. 通过 [XtreamCommandSender](#xtreamcommandsender)

下面分别作简单介绍。

## XtreamResponse

这种方式比较 **被动**，因为一个 `XtreamResponse` 必须和一个 `XtreamRequest` 对应，它是不会独立存在的。

::: tip 适用场景

被动收到客户端消息(客户端主动发送消息)，然后使用对应的 `XtreamResponse` 发送数据；

:::

简单示例:

```java
class SomeFilter implements XtreamFilter {
    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        // 满足某个条件 直接下发数据给客户端; 并结束当前请求的处理流程
        if (someConditionMathed(exchange)) {
            final Mono<ByteBuf> dataToSend = Mono.just(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
            return exchange.response().writeWith(dataToSend);
        }
        // 否则继续执行下一个过滤器
        return chain.filter(exchange);
    }
}
```

## XtreamSession

通过 `XtreamSession` 可以主动下发数据给客户端。

`XtreamSession` 从 `XtreamSessionMananger` 中获取；

简单示例：

```java
class SomeClass {
    Mono<Void> sendMsg() {
        final XtreamSessionManager<XtreamSession> sessionManager = ...;
        return sessionManager.getSessionById("some session id").flatMap(session -> {
            final Mono<ByteBuf> dataToSend = Mono.just(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
            return session.writeWith(dataToSend);
        });
    }
}
```

## XtreamCommandSender

接口定义如下:

```java
public interface XtreamCommandSender<S extends XtreamSession> extends InternalXtreamCommandSender<S> {
    Mono<Void> sendObject(String sessionId, Publisher<Object> data);
}
```

简单示例：

```java
class SomeClass {
    Mono<Void> sendMsg() {
        XtreamCommandSender<XtreamSession> commandSender = getCommandSender();
        return commandSender.sendObject("some session id", Mono.just("hello"));
    }
}
```
