# xtream-codec

<p align="center">
    <a href="https://github.com/hylexus/xtream-codec">
        <img alt="xtream-codec logo" src="docs/src/.vuepress/public/logo.png" width="50%" />
    </a>
</p>

<p align="center">
    <a href="https://www.apache.org/licenses/LICENSE-2.0">
        <img alt="license" src="https://img.shields.io/badge/license-Apache2-blue" />
    </a>
    <a href="https://openjdk.org/projects/jdk/21">
        <img alt="JDK" src="https://img.shields.io/badge/jdk-21-red" />
    </a>
    <a href="https://search.maven.org/search?q=g:%22io.github.hylexus.xtream%22%20AND%20a:%22xtream-codec-core%22">
            <img alt="Maven%20Central" src="https://img.shields.io/maven-central/v/io.github.hylexus.xtream/xtream-codec-core.svg?label=Maven%20Central&color=green" />
    </a>
</p>

## ProjectNaming

项目名来源: `xtream-codec == xtream + codec`

- `xtream == Extensible + Stream`(发音特点合成)
    - `Extensible`: 可扩展
    - `Stream`: <span style="color:red;font-weight:bold;">非阻塞</span>
      的流式编程([projectreactor](https://projectreactor.io))
- `codec == Coder + Decoder`

## Intro

该项目是一个基于 [projectreactor](https://projectreactor.io/) 的、和具体协议无关的、异步的、<span style="color:red;">非阻塞的</span>、TCP/UDP 服务端实现。

同时提供了一个基于 [xtream-codec-server-reactive](xtream-codec-server-reactive) 的 [JT/T 808 协议](ext/jt/jt-808-server-spring-boot-starter-reactive) 服务端实现：

- 支持多版本(**V2013,V2019**)
- 支持分包
- 支持加解密
- 支持指令下发
- 支持苏标附件服务
- 支持链路数据订阅
- 提供了一个基于 **Spring Boot** 的 **Dashboard**

## Modules

```shell
.
├── build-script  ## 构建脚本
├── docs          ## 文档
├── ext           ## 扩展模块
│     └── jt      ## JT/T 扩展
│         └── jt-808-server-spring-boot-starter-reactive  ## JT/T 808 扩展
├── quick-start   ## quick-start 示例
│     └── jt      ## JT/T 示例
│         ├── jt-808-server-quick-start                   ## JT/T 808 服务端示例(不带 dashboard)
│         ├── jt-808-server-quick-start-with-dashboard    ## JT/T 808 服务端示例(带 dashboard)
│         └── jt-808-server-quick-start-with-storage      ## JT/T 808 服务端示例(带 存储:clickhouse,mysql,postgres,minio)
├── debug         ## 调试专用(不用理会)
│     ├── jt      ## JT/T 示例(不用理会)
│     │   └── jt-808-server-spring-boot-starter-reactive-debug   ## JT/T 808 服务端调试(不用理会)
│     ├── xtream-codec-core-debug                       ## xtream-codec-core 模块调试(不用理会)
│     ├── xtream-codec-server-reactive-debug-tcp        ## xtream-codec-server-reactive TCP 调试(不用理会)
│     └── xtream-codec-server-reactive-debug-udp        ## xtream-codec-server-reactive UDP 调试(不用理会)
├── xtream-codec-core                                   ## xtream-codec-core 核心编解码模块
└── xtream-codec-server-reactive                        ## 异步非阻塞的 TCP/UDP 服务端实现
```

## Docs

- Github: https://hylexus.github.io/xtream-codec/
- ~~Gitee: https://hylexus.gitee.io/xtream-codec/~~ (Gitee 暂停 Pages 服务，尚不确定后续能否恢复服务)

## QuickStart

- 带存储的 **JT/T 808** 服务示例: [jt-808-server-quick-start-with-storage](quick-start/jt/jt-808-server-quick-start-with-storage/README.md)
- **自定义** 协议示例
    - Github: https://hylexus.github.io/xtream-codec/core/samples/custom-protocol-sample-01/
    - ~~Gitee: https://hylexus.gitee.io/xtream-codec/core/samples/custom-protocol-sample-01/~~  (Gitee 暂停 Pages 服务，尚不确定后续能否恢复服务)
- **JT/T 808** 协议示例
    - Github: https://hylexus.github.io/xtream-codec/core/samples/custom-protocol-sample-02/
    - ~~Gitee: https://hylexus.gitee.io/xtream-codec/core/samples/custom-protocol-sample-02/~~  (Gitee 暂停 Pages 服务，尚不确定后续能否恢复服务)

## License

`xtream-codec` 使用 [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)。 详情见 [LICENSE](LICENSE) 文件。

