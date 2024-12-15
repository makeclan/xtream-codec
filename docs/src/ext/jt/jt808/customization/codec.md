---
icon: square-binary
article: false
---

# 编解码

## Jt808BytesProcessor

### 作用

`Jt808BytesProcessor` 负责 **转义** 请求消息/响应消息、计算校验码。

要想替换该组件，只需要声明一个 `Jt808BytesProcessor` 类型的 `Bean` 即可。

### 示例

```java
@Component
public class MyJt808BytesProcessor implements Jt808BytesProcessor {

    @Override
    public ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MessageEscapeException {
        // 收到报文 --> 转义
        return ...;
    }

    @Override
    public ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MessageEscapeException {
        // 发送报文 --> 转义
        return ...;
    }

    @Override
    public byte calculateCheckSum(ByteBuf byteBuf) {
        // 计算校验码
        return ...;
    }
}
```

## Jt808RequestDecoder

### 作用

`Jt808RequestDecoder` 负责将请求中的 **字节流** 初步解析为 `XtreamRequest` 对象。

如果内置的实现不符合要求 或者 内置实现返回的 `XtreamRequest` 不符合要求，你可以自定义一个解码器。

### 示例

要自定义 `Jt808RequestDecoder`，只需要将 `Jt808RequestDecoder` 类型的 **Bean** 加入到 **spring** 容器中即可。

```java
@Component
public class MyJt808RequestDecoder implements Jt808RequestDecoder {

    @Override
    public Jt808Request decode(Jt808ServerType serverType, String requestId, ByteBufAllocator allocator, NettyInbound nettyInbound, XtreamInbound.Type requestType, ByteBuf payload, InetSocketAddress remoteAddress) {
        return ...;
    }

}
```

## Jt808ResponseEncoder

### 作用

`Jt808ResponseEncoder` 负责将 处理器返回的对象 编码为 **符合808标准** 的字节流。

### 示例

要自定义 `Jt808ResponseEncoder`，只需要将 `Jt808ResponseEncoder` 类型的 **Bean** 加入到 **spring** 容器中即可。

```java
@Component
public class MyJt808ResponseEncoder implements Jt808ResponseEncoder {

    @Override
    public ByteBuf encode(Object body, Jt808ProtocolVersion version, String terminalId, int flowId, Jt808ResponseBody annotation) {
        return ...;
    }

    @Override
    public ByteBuf encode(Object body, Jt808ProtocolVersion version, String terminalId, Jt808ResponseBody annotation) {
        return ...;
    }

    @Override
    public ByteBuf encode(Object body, Jt808MessageDescriber describer) {
        return ...;
    }
}
```
