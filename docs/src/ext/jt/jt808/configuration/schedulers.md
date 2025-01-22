---
icon: arrows-rotate
article: false
---

# schedulers

## 概览

`jt808-server.schedulers` 配置指的都是 **projectreactor** 中 `reactor.core.scheduler.Scheduler` 的配置。有 4 种类型的
`Scheduler` 配置：

- `request-dispatcher`: **转发请求** 的调度器。可选配置。
- `non-blocking-handler`: **非阻塞型** 处理器的调度器。必需配置。
- `blocking-handler`: **阻塞型** 处理器的调度器。必需配置。
- `event-publisher`: **事件发布器** 的调度器。必需配置。
- 除以上几种外，还以通过 `jt808-server.schedulers.custom-schedulers` 配置自定义的调度器。

```yaml {3}
jt808-server:
  ## ... 省略其他配置 ...
  schedulers:
    ## [转发请求]的调度器
    request-dispatcher:
      type: parallel
      parallel:
        thread-name-prefix: dispatcher
        parallelism: 10
        daemon: true
        reject-blocking-task: true
    ## [非阻塞型]处理器的调度器
    non-blocking-handler:
      type: parallel
      bounded-elastic:
        thread-name-prefix: bounded-elastic
        thread-capacity: 10
        queued-task-capacity: 1024
        reject-blocking-task: true
        daemon: true
        ttl: 1m
      parallel:
        thread-name-prefix: parallel
        parallelism: 10
        daemon: true
        reject-blocking-task: true
      single:
        daemon: true
        reject-blocking-task: true
        thread-name-prefix: single
    ## [阻塞型]处理器的调度器
    blocking-handler:
      type: bounded_elastic
      bounded-elastic:
        thread-name-prefix: blocking-bounded-elastic
        thread-capacity: 10
        queued-task-capacity: 1024
        reject-blocking-task: true
        daemon: true
        ttl: 1m
      parallel:
        thread-name-prefix: blocking-parallel
        parallelism: 10
        daemon: true
        reject-blocking-task: true
      single:
        daemon: true
        reject-blocking-task: true
        thread-name-prefix: blocking-single
    ## [事件发布器]的调度器
    event-publisher:
      type: bounded_elastic
      bounded-elastic:
        thread-name-prefix: event-publisher
        thread-capacity: 10
        queued-task-capacity: 1024
        reject-blocking-task: true
        daemon: true
        ttl: 1m
      parallel:
        thread-name-prefix: event-publisher
        parallelism: 10
        daemon: true
        reject-blocking-task: true
    # 上面 3 个 Scheduler 是必须的
    # 下面 是自定义 Scheduler 的初始化(非必须)
    custom-schedulers: { }
```

## 配置项说明

::: info 提示

`jt808-server.schedulers` 下的各个调度器的配置都是大同小异的，特点如下：

- 指定调度器的类型 `type`, 可用的值有:
    - `bounded_elastic`: 使用 `reactor.core.scheduler.Schedulers#newBoundedElastic` 创建调度器
    - `parallel`: 使用 `reactor.core.scheduler.Schedulers#newParallel` 创建调度器
    - `virtual`: 使用基于虚拟线程的调度器(`Executors#newVirtualThreadPerTaskExecutor()`)
    - `customized`: 用户自定义调度器
    - ~~`single`~~:
        - 仅仅用于测试，不要在生产环境使用
        - 使用 `reactor.core.scheduler.Schedulers#newSingle(ThreadFactory)` 创建调度器
    - ~~`immediate`~~
        - 仅仅用于测试，不要在生产环境使用
        - 使用 `Schedulers#immediate()` 作为调度器
- 给对应 `type` 的 调度器 指定配置
    - 如果 `type` 取值为 `bounded_elastic`, 对应的配置项为 `bounded-elastic.xxx`
    - 如果 `type` 取值为 `parallel`, 对应的配置项为 `parallel.xxx`
    - …… 依此类推

:::

## request-dispatcher

当一个请求到达服务端之后，处理流程可以粗略的概括为下面三个步骤：

- A: **reactor-netty** 的线程负责接收
- B: 随后由 **xtream-server-reactive** 接管
- C: 再经过后续的 `DispatcherXtreamHandler` 负责分发给能处理当前请求的处理器。

默认情况下，**B --> C** 的处理都是在同一个调度器上的，即 **reactor-netty** 的线程上。

当你启用请求转发调度器(`jt808-server.features.request-dispatcher-scheduler.enabled = true`)时，**B --> C**
的处理会在一个单独的调度器上进行，这个调度器就是
`jt808-server.schedulers.request-dispatcher` 配置的调度器。

```yaml {4,10,12}
jt808-server:
  features:
    request-dispatcher-scheduler:
      # 启用请求转发调度器
      enabled: true
  schedulers:
    ## 当且仅当 jt808-server.features.request-dispatcher-scheduler.enabled == true 时生效
    request-dispatcher:
      ## xrd: Xtream-request-dispatcher
      type: parallel
      ## 当且仅当 jt808-server.schedulers.request-dispatcher.type == parallel 时生效
      parallel:
        thread-name-prefix: xrd-parallel
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_POOL_SIZE + 2
        # parallelism: 10
        daemon: true
        reject-blocking-task: true
      ## 当且仅当 jt808-server.schedulers.request-dispatcher.type == bounded_elastic 时生效
      bounded-elastic:
        thread-name-prefix: xrd-bounded-elastic
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE
        # thread-capacity: 10
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE
        # queued-task-capacity: 100000
        reject-blocking-task: true
        daemon: true
        ttl: 1m
      ## 当且仅当 jt808-server.schedulers.request-dispatcher.type == virtual 时生效
      virtual:
        prefix: xrd-vt
      ## !!!仅仅用于测试!!!
      ## 当且仅当 jt808-server.schedulers.request-dispatcher.type == single 时生效
      single:
        thread-name-prefix: xrd-single
        daemon: true
        reject-blocking-task: false
```

## non-blocking-handler

`jt808-server.schedulers.non-blocking-handler` 配置的是 **非阻塞型** 处理器的调度器。

::: info 提示

一个处理器是否是 **非阻塞型** 的，通过 `XtreamBlockingHandlerMethodPredicate` 判断。

:::

```yaml {4}
jt808-server:
  schedulers:
    ## [非阻塞型]处理器的调度器
    non-blocking-handler:
      ## xnbh: Xtream-non-blocking-handler
      type: parallel
      ## 当且仅当 jt808-server.schedulers.non-blocking-handler.type == bounded_elastic 时生效
      bounded-elastic:
        thread-name-prefix: xnbh-bounded-elastic
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE
        # thread-capacity: 10
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE
        # queued-task-capacity: 100000
        reject-blocking-task: true
        daemon: true
        ttl: 1m
      ## 当且仅当 jt808-server.schedulers.non-blocking-handler.type == parallel 时生效
      parallel:
        thread-name-prefix: xnbh-parallel
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_POOL_SIZE + 2
        # parallelism: 10
        daemon: true
        reject-blocking-task: true
      ## 当且仅当 jt808-server.schedulers.non-blocking-handler.type == virtual 时生效
      virtual:
        prefix: xnbh-vt
      ## !!!仅仅用于测试!!!
      ## 当且仅当 jt808-server.schedulers.non-blocking-handler.type == single 时生效
      single:
        daemon: true
        reject-blocking-task: true
        thread-name-prefix: xnbh-single
```

## blocking-handler

`jt808-server.schedulers.blocking-handler` 配置的是 **阻塞型** 处理器的调度器。

::: info 提示

一个处理器是否是 **阻塞型** 的，通过 `XtreamBlockingHandlerMethodPredicate` 判断。

:::

```yaml {4}
jt808-server:
  schedulers:
    blocking-handler:
      ## xbh: Xtream-blocking-handler
      type: bounded_elastic
      ## 当且仅当 jt808-server.schedulers.blocking-handler.type == bounded_elastic 时生效
      bounded-elastic:
        thread-name-prefix: xbh-bounded-elastic
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE
        # thread-capacity: 10
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE
        # queued-task-capacity: 100000
        reject-blocking-task: false
        daemon: true
        ttl: 1m
      ## 当且仅当 jt808-server.schedulers.blocking-handler.type == parallel 时生效
      parallel:
        thread-name-prefix: xbh-parallel
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_POOL_SIZE + 2
        # parallelism: 10
        daemon: true
        reject-blocking-task: false
      ## 当且仅当 jt808-server.schedulers.blocking-handler.type == virtual 时生效
      virtual:
        prefix: xbh-vt
      ## !!!仅仅用于测试!!!
      ## 当且仅当 jt808-server.schedulers.blocking-handler.type == single 时生效
      single:
        thread-name-prefix: xbh-single
        daemon: true
        reject-blocking-task: false
```

## event-publisher

`jt808-server.schedulers.event-publisher`  配置的是 **XtreamEventPublisher** 所使用的调度器。

```yaml {4}
jt808-server:
  schedulers:
    event-publisher:
      ## xep: xtream-event-publisher
      type: bounded_elastic
      ## 当且仅当 jt808-server.schedulers.event-publisher.type == bounded_elastic 时生效
      bounded-elastic:
        thread-name-prefix: xep-bounded-elastic
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE
        # thread-capacity: 10
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE
        # queued-task-capacity: 100000
        reject-blocking-task: true
        daemon: true
        ttl: 1m
      ## 当且仅当 jt808-server.schedulers.event-publisher.type == parallel 时生效
      parallel:
        thread-name-prefix: xep-parallel
        ## @default reactor.core.scheduler.Schedulers.DEFAULT_POOL_SIZE + 2
        # parallelism: 10
        daemon: true
        reject-blocking-task: true
      ## 当且仅当 jt808-server.schedulers.event-publisher.type == virtual 时生效
      virtual:
        prefix: xep-vt
      ## !!!仅仅用于测试!!!
      ## 当且仅当 jt808-server.schedulers.event-publisher.type == single 时生效
      single:
        thread-name-prefix: xep-single
        daemon: true
        reject-blocking-task: true
```

## custom-schedulers

`jt808-server.schedulers.custom-schedulers` 配置的是 **自定义调度器**。

当然你可以不通过配置文件配置自定义调度器，而是直接向 `XtreamSchedulerRegistry` 中注册自定义调度器。

下面配置示例，配置了两个自定义调度器：

```yaml
jt808-server:
  schedulers:
    custom-schedulers:
      # 注册一个名为 `my-virtual-thread-scheduler-1` 的自定义调度器
      my-virtual-thread-scheduler-1:
        # name可以不配置(默认使用 Key)
        # name: my-virtual-thread-scheduler-1
        metrics:
          enabled: true
          prefix: custom1
        remark: "My custom scheduler 1"
        type: virtual
        virtual:
          prefix: my-virtual-thread-scheduler-1
        parallel:
          daemon: true
      # 注册一个名为 `my-scheduler-2` 的自定义调度器
      my-scheduler-2:
        # name可以不配置(默认使用 Key)
        name: my-scheduler-2
        metrics:
          enabled: true
          prefix: custom2
        remark: "My custom scheduler 2"
        type: bounded_elastic
        bounded-elastic:
          thread-name-prefix: my-scheduler-2
```

然后通过 `@Jt808RequestHandlerMapping.scheduler = "my-virtual-thread-scheduler-1"` 引用上面自定义的调度器。

```java
@Component
@Jt808RequestHandler
public class DemoLocationMessageHandler {
    @Jt808RequestHandlerMapping(scheduler = "my-virtual-thread-scheduler-1", messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
    public ServerCommonReplyMessage processMessage0200V2019(
            Jt808Session session,
            Jt808Request request,
            @Jt808RequestBody BuiltinMessage0200 body,
            Jt808RequestEntity<BuiltinMessage0200> requestEntity,
            DemoLocationMsg03 message) {
        log.info("v2019-0x0200: {}", message);
        log.info("v2019-0x0200: threadName={} thread={}", Thread.currentThread().getName(),Thread.currentThread());
        // 当前方法被调度到了 `my-virtual-thread-scheduler-1` 这个基于虚拟线程的调度器上
        // 所以这里可以阻塞
        final Byte result = this.locationService.processLocationMessage(session, body).block();
        return ServerCommonReplyMessage.of(request, Objects.requireNonNull(result));
    }
}
```
