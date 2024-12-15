---
icon: arrows-rotate
article: false
---

# 调度器

## 作用

这里介绍的是 **reactor** 中 `Scheduler` 的配置。`Scheduler` 决定了你的代码在哪个线程上执行。

只要将你自定义的调度器注册到 `XtreamSchedulerRegistry` 中即可。

## 自定义方式1

直接调用 `XtreamSchedulerRegistry.registerScheduler()` 方法注册。

## 自定义方式2

提供了一种通过配置文件快速自定义的配置方式。

下面示例中注册了两个调度器，名称分别为 `my-scheduler-1` 和 `my-scheduler-2`。

```yaml
jt808-server:
  schedulers:
    custom-schedulers:
      my-scheduler-1:
        # name可以不配置(默认使用 Key)
        # name: my-scheduler-1
        type: parallel
        parallel:
          daemon: true
          # ...
      my-scheduler-2:
        name: my-scheduler-2
        type: bounded_elastic
        bounded-elastic:
          thread-name-prefix: my-scheduler-2
          # ...
```

然后就可以指定处理器运行在你自定义的 调度器上了：

```java {7}
@Component
@Jt808RequestHandler
public class Jt808QuickStartRequestHandler {
    @Jt808RequestHandlerMapping(
            messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019,
            // 使用 XtreamSchedulerRegistry 中注册的名为 "my-scheduler-1" 的调度器
            scheduler = "my-scheduler-1"
    )
    @Jt808ResponseBody(messageId = 0x8001, maxPackageSize = 1000)
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
}
```
