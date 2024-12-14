---
article: false
icon: rocket
tag:
  - JT/T 808
---

# 快速开始

此处将展示一个 最少配置 的 **808协议** 消息处理服务的搭建。

## 创建工程

创建一个空的 **spring-boot** 工程。

::: tip 传送门

可以使用 [Spring Initializer](https://start.spring.io) 快速初始化一个 Spring Boot 工程。

:::

## 添加依赖

引入为 **808协议** 提供的 `jt-808-server-spring-boot-starter-reactive`。

最新版本请点击
[![Maven Central](https://img.shields.io/maven-central/v/io.github.hylexus.xtream/jt-808-server-spring-boot-starter-reactive.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.hylexus.xtream%22%20AND%20a:%22jt-808-server-spring-boot-starter-reactive%22)
查看。

::: tabs#starter

@tab Maven

```xml

<dependency>
    <groupId>io.github.hylexus.xtream</groupId>
    <artifactId>jt-808-server-dashboard-spring-boot-starter-reactive</artifactId>
    <version>0.0.1-beta.7</version>
</dependency>
```

@tab Gradle

```groovy

api("io.github.hylexus.xtream:jt-808-server-spring-boot-starter-reactive:0.0.1-beta.7")
```

:::

## 配置

该示例中，你可以不用做任何额外配置。

下面是几个比较关键配置(`application.yaml`)的默认值：

```yaml
jt808-server:
  ## 指令服务器
  instruction-server:
    ## 指令服务器-TCP
    tcp-server:
      # 绑定所有网卡(不只是127.0.0.1)
      host: 0.0.0.0
      port: 3927
    ## 指令服务器-UDP
    udp-server:
      # 绑定所有网卡(不只是127.0.0.1)
      host: 0.0.0.0
      port: 3721
  ## 附件服务器(苏标扩展)
  attachment-server:
    ## 附件服务器-TCP
    tcp-server:
      # 绑定所有网卡(不只是127.0.0.1)
      host: 0.0.0.0
      port: 3824
    ## 附件服务器-UDP
    udp-server:
      # 绑定所有网卡(不只是127.0.0.1)
      host: 0.0.0.0
      port: 3618
```

## 编写消息处理器

在 **spring-boot** 能扫描到的包中，随便新建一个 **java类**(本示例中命名为 `Jt808QuickStartRequestHandler.java`)。内容如下：

```java
@Component
@Jt808RequestHandler
public class Jt808QuickStartRequestHandler {

    /**
     * 位置上报(V2019)
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
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

    /**
     * 0:成功/确认; 1:失败; 2:消息有误; 3:不支持; 4:报警处理确认
     */
    private Mono<Byte> processLocationMessage(Jt808Session session, BuiltinMessage0200 body) {
        // 业务逻辑...
        return Mono.just((byte) 0);
    }
}

```

## 测试

### 启动项目

至此，对 **808消息** 的处理服务已经搭建完毕。启动你新建的 **spring-boot** 项目开始测试，下图是启动项目后的效果：

![quick-start-app-startup](/img/ext/jt/jt808/quick-start/quick-start-app-startup.png)

### 发报文

### 服务端日志
