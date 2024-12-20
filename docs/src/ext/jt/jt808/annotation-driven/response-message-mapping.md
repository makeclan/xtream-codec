---
icon: at
article: false
---

# 响应消息映射

## @Jt808ResponseBody

### 作用

该注解的作用类似于 **WebFlux/WebMvc** 中的 `@ResponseBody` 。

表明被该注解标记的类可以作为 **响应体**(**JT/T 808** 协议中的消息体)。

### 属性介绍

| 属性名                       | 类型     | 描述                       | 取值示例     |
|---------------------------|--------|--------------------------|----------|
| **messageId**             | `int`  | 响应消息的 **消息ID**           | `0x8001` |
| **encryptionType**        | `byte` | 消息体加密类型                  | `0b000`  |
| **maxPackageSize**        | `int`  | 单个消息包的最大字节数, 超过该值会自动分包发送 | `1024`   |
| **reversedBit15InHeader** | `byte` | 消息头中的保留位(第15个bit)        | `0`      |

### 作用范围

默认按照下面顺序来解析该注解：

- 处理器方法上的注解
- 处理器方法返回类型上的注解

#### 处理器方法上的注解

下面示例中将该注解标记在了处理器方法上：

```java {2}
@Jt808RequestHandlerMapping(messageIds = 0x0002)
@Jt808ResponseBody(messageId = 0x8001)
public Mono<ServerCommonReplyMessage> processMessage0002(Jt808Request request, @Jt808RequestBody BuiltinMessage0002 requestBody) {
    log.info("receive message [0x0002]: {}", requestBody);
    final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(request);
    return Mono.just(responseBody);
}
```

#### 处理器方法返回类型上的注解

下面示例中，处理器方法上并没有该注解，而是标记在了处理器方法返回类型的实体类上：

```java {1}
@Jt808ResponseBody(messageId = 0x8001)
public static class BuiltinMessage8001 {
    /**
     * 1. 应答流水号 WORD    对应的终端消息的流水号
     */
    @Preset.JtStyle.Word
    protected int clientFlowId;

    /**
     * 2. 应答id WORD     对应的终端消息的 ID
     */
    @Preset.JtStyle.Word
    protected int clientMessageId;

    /**
     * 3. 结果  BYTE    0:成功/确认; 1:失败; 2:消息有误; 3:不支持; 4:报警处理确认
     */
    @Preset.JtStyle.Byte
    protected short result;
}
```

这样一来，方法上就不用标记该注解了。

```java
@Jt808RequestHandlerMapping(messageIds = 0x0002)
public Mono<ServerCommonReplyMessage> processMessage0002(Jt808Request request, @Jt808RequestBody BuiltinMessage0002 requestBody) {
    log.info("receive message [0x0002]: {}", requestBody);
    final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(request);
    return Mono.just(responseBody);
}
```

### 实现原理

实现原理见 `Jt808ResponseBodyHandlerResultHandler` 。

## Jt808ResponseEntity

### 作用

该类型的作用类似于 **WebFlux/WebMvc** 中的 `org.springframework.http.ResponseEntity`。

::: tip

当你的处理器返回 `Jt808ResponseEntity` 类型时，就不再需要 `@Jt808ResponseBody(messageId = 0x8001)` 这种注解指定响应头信息了。

而是通过 `Jt808ResponseEntity` **动态设定** 响应头信息。

当然，你加了 `@Jt808ResponseBody(messageId = 0x8001)` 也没关系(自动忽略)。

:::

### 示例

```java {3,13-18}
// 注意: 这里不再需要 @Jt808ResponseBody(messageId = 0x8001)
@Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
public Mono<Jt808ResponseEntity<ServerCommonReplyMessage>> processMessage0200V2019(
        Jt808Session session,
        Jt808Request request,
        @Jt808RequestBody BuiltinMessage0200 body,
        Jt808RequestEntity<BuiltinMessage0200> requestEntity) {
    log.info("v2019-0x0200: {}", body);
    log.info("v2019-0x0200: {}", requestEntity);

    return this.locationService.processLocationMessage(session, body).map(result -> {
        // ...
        return Jt808ResponseEntity
                // 这里动态指定响应消息中的属性
                .messageId(0x8001)
                .maxPackageSize(1000)
                // 消息体
                .body(ServerCommonReplyMessage.of(request, (byte) 0));
    });
}
```

## 不回复消息

有些时候，可能仅仅是处理请求，但并不需要给客户端回复。
这时只需要让处理器方法，返回 `Void / void / Mono<Void>` 即可。

实现原理见 `EmptyXtreamHandlerResultHandler`

## 手动回复消息

只要能拿到 `XtreamOutbound` 然后手动调用 `XtreamOutbound#writeWith` 即可。

`XtreamResponse` 和 `Jt808Session` 都是 `XtreamOutbound` 的实现，可以直接注入使用。

```java {3,12}
@Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
public Mono<Void> processMessage0200V2019(
        XtreamResponse response
) {
    final CompositeByteBuf compositeByteBuf = exchange.bufferFactory().compositeBuffer(2);
    // 这里构造响应数据
    compositeByteBuf.addComponents(true, byteBuf.copy());
    compositeByteBuf.addComponents(true, Unpooled.wrappedBuffer(new byte[]{1, 1, 1, 1}));
    // 写数据改客户端
    return response
            .writeWith(Mono.just(compositeByteBuf));
}
```
