---
icon: arrows-rotate
article: false
---

# schedulers

## 概览

`schedulers` 指的是 **projectreactor** 中 `reactor.core.scheduler.Scheduler` 的配置。

```yaml
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

## request-dispatcher

todo: 补充配置项详细说明

## non-blocking-handler

todo: 补充配置项详细说明

## blocking-handler

todo: 补充配置项详细说明

## event-publisher

todo: 补充配置项详细说明

## custom-schedulers

todo: 补充配置项详细说明
