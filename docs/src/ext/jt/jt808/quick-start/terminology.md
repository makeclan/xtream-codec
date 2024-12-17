---
icon: file-lines
---

# 基本术语

## Jt808Request

`Jt808Request` 扩展了 [XtreamRequest](/guide/server/quick-start/terminology.md#xtreamrequest) 接口。

新增/修改了如下方法：

```java
public interface Jt808Request extends XtreamRequest {

    /**
     * 同一次请求和响应 该值应该确保一致（包括分包消息）。
     *
     * @see #requestId()
     */
    String traceId();

    // 指令服务器/附件服务器
    Jt808ServerType serverType();

    // JT/T 808 协议消息头
    Jt808RequestHeader header();

    default int messageId() {
        return this.header().messageId();
    }

    default String terminalId() {
        return this.header().terminalId();
    }

    // JT/T 808 协议消息体
    default ByteBuf body() {
        return this.payload();
    }

    // 服务端计算的校验码
    int calculatedCheckSum();

    // 报文中的校验码
    int originalCheckSum();

    @Override
    Jt808RequestBuilder mutate();

}
```

## Jt808Session

`Jt808Session` 扩展了 [XtreamSession](../../../../guide/server/quick-start/terminology.md#xtreamsession) 接口。定义如下:

```java
public interface Jt808Session extends XtreamSession {

    // 指令服务器/附近服务器
    Jt808ServerType role();

    boolean verified();

    Jt808Session verified(boolean verified);

    // 上次通信事件
    Jt808Session lastCommunicateTime(Instant current);

    // 终端ID
    String terminalId();

    // JT/T 808 协议版本
    Jt808ProtocolVersion protocolVersion();
}
```

## Jt808SessionManager

`Jt808SessionManager` 继承自 [XtreamSessionManager](../../../../guide/server/core-component/session-manager.md) 接口。定义如下:

```java
public interface Jt808SessionManager extends XtreamSessionManager<Jt808Session> {

    default Stream<Jt808Session> list(int page, int pageSize, Predicate<Jt808Session> filter) {
        return this.list().filter(filter).sorted(Comparator.comparing(Jt808Session::terminalId)).skip((long) (page - 1) * pageSize).limit(pageSize);
    }

    default Stream<Jt808Session> list(int page, int pageSize) {
        return this.list().sorted(Comparator.comparing(Jt808Session::terminalId)).skip((long) (page - 1) * pageSize).limit(pageSize);
    }

    default <T> Stream<T> list(int page, int pageSize, Predicate<Jt808Session> filter, Function<Jt808Session, T> converter) {
        return this.list().filter(filter).sorted(Comparator.comparing(Jt808Session::terminalId)).skip((long) (page - 1) * pageSize).limit(pageSize).map(converter);
    }

    default <T> Stream<T> list(int page, int pageSize, Function<Jt808Session, T> converter) {
        return this.list().sorted(Comparator.comparing(Jt808Session::terminalId)).skip((long) (page - 1) * pageSize).map(converter).limit(pageSize);
    }

}
```

## Jt808AttachmentSessionManager

`Jt808AttachmentSessionManager` 指的是对附件服务器的会话管理。作用和 `Jt808SessionManager` 类似。

## 其他

- 和请求对应的响应，请参考 [XtreamResponse](../../../../guide/server/quick-start/terminology.md#xtreamresponse)
- [XtreamexChange](../../../../guide/server/quick-start/terminology.md#xtreamexchange)
