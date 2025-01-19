# jt-808-server-quick-start-with-storage-nonblocking

## 介绍

当前项目是使用 **xtream-codec** 处理 **JT/T 808** 协议的示例项目。

代码可能看起来有点复杂，但是其实核心代码没几行。

大多数代码都是为了支持多种存储介质，写的适配代码。

非阻塞式编程:

- 数据访问:
    - [R2DBC](https://r2dbc.io/)
    - [reactive-mybatis-support](https://github.com/chenggangpro/reactive-mybatis-support)
- Web: [Spring-WebFlux](https://docs.spring.io/spring-boot/reference/web/reactive.html)

## 功能描述

下面几个组件都有开关配置是否启用。

- 请求和响应报文存储
    - [Clickhouse](docker/quick-start-3/init.d/clickhouse/init-clickhouse.sql)
    - [Postgres](docker/quick-start-3/init.d/postgres/init-postgres.sql)
    - [Mysql](docker/quick-start-3/init.d/mysql/init-mysql.sql)
    - Logging(仅仅打印了个日志)
- 苏标附件
    - Minio
    - Logging(仅仅打印了个日志)

## 基于源码启动

- 准备中间件
    - `Clickhouse`,`Postgres`,`Mysql` 至少选择一个：用来存储请求和响应报文
    - `Minio` 用来存储苏标附件
- 修改配置文件: [application-docker_compose.yaml](src/main/resources/application-docker_compose.yaml)
    - 默认启用了 Clickhouse 和 Minio，看情况修改
    - 某个中间件是否启用取决于 `demo-app-with-storage.feature-control.xxx.enabled` 配置项
- 启动服务: 运行 [Jt808ServerQuickStartWithStorageApp.java](src/main/java/io/github/hylexus/xtream/quickstart/ext/jt808/withstorage/Jt808ServerQuickStartWithStorageApp.java)
- 访问 UI: http://localhost:8888
- 使用客户端发送报文

## 基于Docker启动

### 启动方式1

命令行直接启动已经构建好的镜像。

参考 [quick-start-1/quick-start.sh](docker/quick-start-1/quick-start.sh)

### 启动方式2

命令行直接启动已经构建好的镜像(挂载自定义配置文件)。

参考 [quick-start-2/quick-start.sh](docker/quick-start-2/quick-start.sh)

### 启动方式3(docker-compose)

参考 [quick-start-3/docker-compose.yaml](docker/quick-start-3/docker-compose.yaml)

- 如果你不想启用所有中间件,在 [.env](docker/quick-start-3/.env) 文件中修改 `COMPOSE_PROFILES` 变量
- 同时记得修改环境变量中的各个开关配置(`FEATURE_CONTROL_XXX_ENABLED`)
