---
icon: paperclip
article: false
---

# attachment-server

## 概览

`attachment-server` 指的是(苏标等扩展)附件服务器(区别于指令服务器)的配置。

支持 `TCP` 和/或 `UDP` 两种模式。可以同时开启，也可以只启用其中一个。

```yaml {3}
jt808-server:
  ## ... 省略其他配置 ...
  attachment-server:
    # TCP 服务配置(附件服务器)
    tcp-server:
      enabled: true
      host: 0.0.0.0
      port: 3824
      ## 指令报文的最大长度
      ## @see: io.netty.handler.codec.DelimiterBasedFrameDecoder.maxFrameLength
      max-instruction-frame-length: 1024
      ## 苏标扩展码流消息 0x30316364 的最大长度(66560 = 1024 * 65)
      ## @see io.netty.handler.codec.DelimiterBasedFrameDecoder.maxFrameLength
      max-stream-frame-length: 66560
      ## 附件务器的会话空闲状态检查器(TCP)
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
    # UDP 服务配置(附件服务器)
    udp-server:
      enabled: true
      # 绑定所有网卡
      host: 0.0.0.0
      # UDP 服务端口
      port: 3618
      ## 附件服务器的会话空闲状态检查器(UDP)
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

## 备注

::: info 备注

附件服务器的配置 和 指令服务器的配置 基本一致，只是 **TCP** 类型的附件服务器多出了一个 `max-stream-frame-length` 配置。

其他配置项参考指令服务器配置，此处不再赘述。

:::

## max-stream-frame-length

```yaml {4}
jt808-server:
  attachment-server:
    tcp-server:
      max-stream-frame-length: 66560
```

- 该配置项限制 苏标扩展码流消息 `0x30316364` 的最大长度
- 默认值为:**66560 (1024 * 65)**
