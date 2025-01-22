---
icon: square-binary
article: false
---

# instruction-server

## 概览

`instruction-server` 指的是指令服务器(区别于附件服务器)的配置。

支持 `TCP` 和/或 `UDP` 两种模式。可以同时开启，也可以只启用其中一个。

```yaml {3}
jt808-server:
  ## ... 省略其他配置 ...
  instruction-server:
    # TCP 服务配置(指令服务器)
    tcp-server:
      # 是否启动 TCP 服务
      enabled: true
      # 绑定所有网卡
      host: 0.0.0.0
      # TCP 服务端口
      port: 3927
      ## 指令报文的最大长度
      ## @see: io.netty.handler.codec.DelimiterBasedFrameDecoder.maxFrameLength
      max-instruction-frame-length: 1024
      ## 指令服务器的会话空闲状态检查器(TCP)
      session-idle-state-checker:
        reader-idle-time: 0s
        writer-idle-time: 0s
        all-idle-time: 10m
      ## 请求处理线程
      loop-resources:
        select-count: 1
        worker-count: 10
        thread-name-prefix: xtream-tcp
        daemon: true
        colocate: true
        prefer-native: true
    # UDP 服务配置(指令服务器)
    udp-server:
      # 是否启动 UDP 服务
      enabled: true
      # 绑定所有网卡
      host: 0.0.0.0
      # UDP 服务端口
      port: 3721
      ## 指令服务器的会话空闲状态检查器(UDP)
      session-idle-state-checker:
        max-idle-time: 10m
        check-interval: 10s
        check-backoff-time: 10s
      ## 请求处理线程
      loop-resources:
        select-count: 1
        worker-count: 10
        thread-name-prefix: xtream-udp
        daemon: true
        colocate: true
        prefer-native: true
```

## tcp-server

### 基础配置

- `enabled`: 是否启用 **TCP** 服务
- `host`: **TCP** 协议监听的 **IP** 地址。默认为 `0.0.0.0`，即监听所有网卡
- `port`: **TCP** 协议监听的 **端口**。默认为 `3927`
- `max-instruction-frame-length`: 指令报文的最大长度
    - 默认为 `1024`
    - 具体含义参考 `io.netty.handler.codec.DelimiterBasedFrameDecoder.maxFrameLength`

### session-idle-state-checker

该配置用来控制会话空闲状态检查器。

三个时间配置的具体含义参考: `io.netty.handler.timeout.IdleStateHandler`

```yaml {4-10}
jt808-server:
  instruction-server:
    tcp-server:
      session-idle-state-checker:
        # 配置示例: 45s(45秒), 3m(3分钟), 1h(1小时)
        reader-idle-time: 0s
        # 配置示例: 45s(45秒), 3m(3分钟), 1h(1小时)
        writer-idle-time: 0s
        # 配置示例: 45s(45秒), 3m(3分钟), 1h(1小时)
        all-idle-time: 10m
```

### loop-resources

该配置项用来配置请求处理线程的相关参数。

具体含义参考: `reactor.netty.resources.LoopResources`

```yaml {4-10}
jt808-server:
  instruction-server:
    tcp-server:
      loop-resources:
        select-count: 1
        worker-count: 10
        thread-name-prefix: xtream-tcp
        daemon: true
        colocate: true
        prefer-native: true
```

## udp-server

### 基础配置

- `enabled`: 是否启用 **UDP** 服务
- `host`: **UDP** 协议监听的 **IP** 地址。默认为 `0.0.0.0`，即监听所有网卡
- `port`: **UDP** 协议监听的 **端口**。默认为 `3721`

### session-idle-state-checker

该配置用来控制会话空闲状态检查器。

::: warning 提示

- 配置项的含义和 **tcp-server.session-idle-state-checker** 不同。
- **UDP** 是无状态的，这里的会话状态控制逻辑是：
    - **UDP** 客户端第一次发送数据包，服务端会记录客户端的`(IP, Port)`信息
    - 后续的请求都是基于该会话信息进行判断的。

:::

```yaml {4-14}
jt808-server:
  instruction-server:
    udp-server:
      ## 指令服务器的会话空闲状态检查器(UDP)
      session-idle-state-checker:
        # 多久后，如果客户端没有发送数据包，则认为该会话已经失效
        # 配置示例: 45s(45秒), 3m(3分钟), 1h(1小时)
        max-idle-time: 10m
        # 多久检测一次是否有会话空闲需要关闭
        # 配置示例: 45s(45秒), 3m(3分钟), 1h(1小时)
        check-interval: 10s
        # 具体含义参考 reactor.util.retry.RetryBackoffSpec#maxBackoff
        # 配置示例: 45s(45秒), 3m(3分钟), 1h(1小时)
        check-backoff-time: 10s
```

### loop-resources

该配置项用来配置请求处理线程的相关参数。

具体含义参考: `reactor.netty.resources.LoopResources`

```yaml
jt808-server:
  instruction-server:
    tcp-server:
      loop-resources:
        select-count: 1
        worker-count: 10
        thread-name-prefix: xtream-udp
        daemon: true
        colocate: true
        prefer-native: true
```
