# jt-808-server-quick-start-with-storage

当前项目是使用 **xtream-codec** 处理 **JT/T 808** 协议的示例项目。

## 功能描述

下面几个组件都有开关配置是否启用。

- 请求和响应报文存储
    - [Clickhouse](src/main/resources/jt-808-server-quick-start-with-storage-docker-compose/init.d/clickhouse/init-clickhouse.sql)
    - [Postgres](src/main/resources/jt-808-server-quick-start-with-storage-docker-compose/init.d/postgres/init-postgres.sql)
    - [Mysql](src/main/resources/jt-808-server-quick-start-with-storage-docker-compose/init.d/mysql/init-mysql.sql)
    - Logging(仅仅打印了个日志)
- 苏标附件
    - Minio
    - Logging(仅仅打印了个日志)

## 备注

- 调试用到的数据存储服务都在 [docker-compose.yaml](src/main/resources/jt-808-server-quick-start-with-storage-docker-compose/docker-compose.yaml) 中定义；
- 如果你不想启用所有中间件
    - 在 [.env](src/main/resources/jt-808-server-quick-start-with-storage-docker-compose/.env) 文件中修改 `COMPOSE_PROFILES` 变量。
    - 同时记得修改 [application-docker_compose.yaml](src/main/resources/application-docker_compose.yaml) 文件中的 `demo-app-with-storage.feature-control.xxx.enabled` 配置项
