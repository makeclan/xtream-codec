---
icon: up-down-left-right
article: false
---

# 项目定位

## 项目名来源

`xtream-codec == xtream + codec`

- `xtream == Extensible + Stream`(发音特点合成)
    - `Extensible`: 可扩展
    - `Stream`: <span style="color:red;font-weight:bold;">非阻塞</span>
      的流式编程([projectreactor](https://projectreactor.io))
- `codec == Coder + Decoder`

## 项目定位

该项目的定位是一个 **和具体协议无关的** `TCP/UDP` 编解码库。

- [xtream-codec-core](/guide/core/quick-start/intro.md): 基于注解的编解码功能库
- [xtream-codec-server-reactive](/guide/server/quick-start/terminology.md): `TCP/UDP` 服务端
    - 基于 [ReactorNetty](https://projectreactor.io/)
    - 异步、<span style="color:red;">非阻塞</span>

## 内置协议

::: tip

如果你想让本项目内置某个常见协议的实现，欢迎提 issue:

- [Gitee](https://gitee.com/hylexus/xtream-codec/issues)
- [Github](https://github.com/hylexus/xtream-codec/issues)

:::

本项目会考虑对主流协议的内置支持，目前已经内置支持的协议有:

- **JT/T 808** 协议
    - 支持多版本(**V2013,V2019**)
    - 支持分包
    - 支持加解密
    - 支持指令下发
    - 支持苏标附件服务
    - 支持链路数据订阅
    - 提供了一个基于 **Spring Boot** 的 **Dashboard**

后续会逐步增加其他协议的内置支持。

