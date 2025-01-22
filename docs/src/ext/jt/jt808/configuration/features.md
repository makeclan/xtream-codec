---
icon: toggle-on
article: false
---

# features

## 概览

```yaml {3}
jt808-server:
  ## ... 省略其他配置 ...
  features:
    ### 转发请求时使用指定的调度器(而不是上游默认的Reactor调度器)
    request-dispatcher-scheduler:
      enabled: false
    ### 请求日志(仅仅用于调试)
    request-logger:
      enabled: true
    ### 请求合并(如果不启用的话，你需要手动合并子包请求)
    request-combiner:
      enabled: true
      # 子包暂存器
      sub-package-storage:
        # 最大缓存数量
        maximum-size: 1024
        # 缓存过期时间
        ttl: 45s
```

## request-dispatcher-scheduler

默认情况下，转发请求时使用的调度器是 Reactor 请求入口处所使用的调度器。

通过 `jt808-server.features.request-dispatcher-scheduler.enabled = true` 可以启用自定义调度器。

::: info 提示

- 这个配置项只是个开关，默认值为 `false`。
- 其对应的调度器配置在 `jt808-server.schedulers.request-dispatcher` 配置项中指定。
- 参考 [schedulers#request-dispatcher](./schedulers.md#request-dispatcher)

:::

## request-logger

开启之后，会在日志中打印请求的详细信息。格式如下：

```text
2025-01-21 22:37:43.600 [x8i-tcp-nio-2] INFO  i.g.h.x.c.e.j.e.l.Jt808RequestLoggerListener:41 - ===> Receive [TCP/0x0200(定位数据上报)] message: requestId = 2c022850b1d14812aa3c044995e6a527, traceId = d7a6ff29ffc54121a4e2e4d40ce04203, remoteAddr = /127.0.0.1:61391, payload = 7e02004086010000000001893094655200e4000000000000000101d907f2073d336c000000000000211124114808010400000026030200003001153101002504000000001404000000011504000000fa160400000000170200001803000000ea10ffffffffffffffffffffffffffffffff02020000ef0400000000f31b017118000000000000000000000000000000000000000000000000567e
2025-01-21 22:37:43.657 [xnbh-parallel-1] INFO  i.g.h.x.c.e.j.e.l.Jt808RequestLoggerListener:54 - <=== Send [TCP] message: requestId = 2c022850b1d14812aa3c044995e6a527, traceId = d7a6ff29ffc54121a4e2e4d40ce04203, remoteAddr = /127.0.0.1:61391, payload = 7e800140050100000000018930946552000100e4020000397e
```

## request-combiner

该配置项用来控制和分包请求合并相关的配置。

```yaml {3-13}
jt808-server:
  features:
    request-combiner:
      # 是否启用分包请求合并功能
      # 如果不启用的话，你需要手动合并子包请求
      enabled: true
      # 子包暂存器配置
      sub-package-storage:
        # 暂存器中最大的缓存数量
        maximum-size: 1024
        # 暂存器中子包的过期时间(长时间没有使用的缓存条目将被丢弃)
        # 配置示例: 45s(45秒), 3m(3分钟), 1h(1小时)
        ttl: 45s
```
