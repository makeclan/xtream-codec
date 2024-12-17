---
icon: info-circle
article: false
---

# 概览

在 **Xtream** 中，基于注解的方式处理 **JT/T 808** 请求，大致像下面这样：

```java
@Component
@Jt808RequestHandler
public class Jt808QuickStartRequestHandler {

    @Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
    @Jt808ResponseBody(messageId = 0x8001)
    public Mono<ServerCommonReplyMessage> processMessage0200V2019(
            Jt808Session session,
            Jt808Request request,
            @Jt808RequestBody BuiltinMessage0200 body) {
        log.info("v2019-0x0200: {}", body);
        return this.processLocationMessage(session, body).map(result -> {
            // ...
            return ServerCommonReplyMessage.of(request, result);
        });
    }
    
    /**
     * 0:成功/确认; 1:失败; 2:消息有误; 3:不支持; 4:报警处理确认
     */
    private Mono<Byte> processLocationMessage(Jt808Session session, BuiltinMessage0200 body) {
        // 业务逻辑...
        return Mono.just((byte) 0);
    }
}
```

更多细节，请看后续文档。
