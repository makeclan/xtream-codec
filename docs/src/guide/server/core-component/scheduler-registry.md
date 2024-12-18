---
icon: table-list
article: false
---

# XtreamSchedulerRegistry

## 介绍

从名称可以看出来 `XtreamSchedulerRegistry` 是 `Scheduler` 的注册表。`Scheduler` 指的是 `reactor.core.scheduler.Scheduler`。

::: tip

- 如果你不熟悉 `Scheduler`，可以参考 [官方文档 -- Threading and Schedulers](https://projectreactor.io/docs/core/release/reference/coreFeatures/schedulers.html)。
- 如果你不想了解 `Scheduler`，可以先简单粗暴的将其和 `线程池` 的概念对应。`线程池` 和 `Scheduler` 有相似之处，但他们并不是一回事。
- 先有个概念就行，大致上涉及到以下方面:
    - 你的代码什么时候执行？
    - 你的代码在哪个线程上执行？

:::

接口定义如下：

```java
public interface XtreamSchedulerRegistry {
    /**
     * 请求转发 使用的默认调度器
     *
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.impl.RequestDispatcherSchedulerFilter
     */
    @Nullable
    default Scheduler requestDispatcherScheduler() {
        return this.getScheduler(SCHEDULER_NAME_REQUEST_DISPATCHER).orElse(null);
    }

    /**
     * 非阻塞型处理器 使用的默认调度器
     *
     * @see XtreamRequestHandler#nonBlockingScheduler()
     * @see XtreamRequestHandlerMapping#scheduler()
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate
     */
    default Scheduler defaultNonBlockingScheduler() {
        return this.getScheduler(SCHEDULER_NAME_NON_BLOCKING).orElseThrow();
    }

    /**
     * 阻塞型处理器 使用的默认调度器
     *
     * @see XtreamRequestHandler#blockingScheduler()
     * @see XtreamRequestHandlerMapping#scheduler()
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate
     */
    default Scheduler defaultBlockingScheduler() {
        return this.getScheduler(SCHEDULER_NAME_BLOCKING).orElseThrow();
    }

    /**
     * 事件发布器专用的调度器
     *
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher
     */
    default Scheduler eventPublisherScheduler() {
        return this.getScheduler(SCHEDULER_NAME_EVENT_PUBLISHER).orElseThrow();
    }

    /**
     * @param name 调度器名称
     */
    Optional<Scheduler> getScheduler(String name);

    /**
     * @param name      要注册的调度器的名称
     * @param scheduler 要注册的调度器
     * @return {@code true}: 成功注册; {@code false}: 已经存在同名调度器
     */
    default boolean registerScheduler(String name, Scheduler scheduler) {
        return this.registerScheduler(SchedulerConfig.ofDefault(name), scheduler);
    }

    boolean registerScheduler(SchedulerConfig config, Scheduler scheduler);

    /**
     * @param name 要移除的调度器名称
     * @return {@code true}: 成功移除; {@code false}: 要移除的调度器不存在
     * @throws UnsupportedOperationException 尝试移除默认调度器
     */
    boolean removeScheduler(String name) throws UnsupportedOperationException;

    // ...
}
```

## 必需组件

不难看出，`XtreamSchedulerRegistry` 其实就是对 `Scheduler` 增删改查。 但是并不是所有的 `Scheduler` 都是可以移除的。

下面三个`Scheduler` 是必需的：

- `defaultNonBlockingScheduler()`: 非阻塞型处理器用到的调度器
- `defaultBlockingScheduler()`: 阻塞型处理器用到的调度器
- `eventPublisherScheduler()`: 事件发布器专用的调度器

