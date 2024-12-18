---
date: 2024-12-10
icon: fa-brands fa-resolving
---

# 参数解析器

## 介绍

这里说的 **参数解析器** 指的是 `XtreamHandlerMethodArgumentResolver`。

名字比较长，但从命名中可以看出来它本质上是一个 **方法参数的解析器**。

下面代码段中 `processMessage0200V2019` 方法的所有参数都是通过 `XtreamHandlerMethodArgumentResolver` 解析出来的：

```java {8,9,10,11,12,13,14,15,16,17,18,19,20}
@Component
@Jt808RequestHandler
public class DemoJt808RequestHandler {

    @Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
    @Jt808ResponseBody(messageId = 0x8001, maxPackageSize = 1000)
    public Mono<ServerCommonReplyMessage> processMessage0200V2019(
            XtreamExchange exchange,
            XtreamSession xtreamSession,
            XtreamRequest xtreamRequest,
            Jt808Request jt808Request,
            DefaultXtreamRequest defaultXtreamRequest,
            XtreamResponse xtreamResponse,
            DefaultXtreamResponse defaultXtreamResponse,
            @Jt808RequestBody DemoLocationMsg01 msg01,
            @Jt808RequestBody DemoLocationMsg02 msg02,
            @Jt808RequestBody ByteBuf buf01,
            @Jt808RequestBody ByteBuf buf02,
            @Jt808RequestBody(bufferAsSlice = false) ByteBuf buf03,
            @Jt808RequestBody(bufferAsSlice = false) ByteBuf buf04) {

        log.info("v2019-0x0200: {}", msg01);
        assertNotSame(buf01, buf02);
        assertNotSame(buf01, buf03);
        assertSame(buf03, buf04);
        assertSame(exchange.request(), xtreamRequest);
        assertSame(exchange.request(), jt808Request);
        assertSame(exchange.request(), defaultXtreamRequest);
        assertSame(exchange.response(), xtreamResponse);
        assertSame(exchange.response(), defaultXtreamResponse);
        final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(jt808Request);
        return Mono.just(responseBody);
    }
    
}
```

## 内置实现

![](/img/server/annotation-driven/handler-method-argument-resolver.png)
