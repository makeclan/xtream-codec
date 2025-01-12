#!/usr/bin/env sh

LOCAL_IP_ADDR=192.168.71.59

docker pull registry.cn-hangzhou.aliyuncs.com/xtream-codec/jt-808-server-quick-start-with-storage-blocking:latest

docker run --rm -p 8080:8080 \
-p 3927:3927 \
-p 3721:3721 \
-p 3824:3824 \
-p 3618:3618 \
-e FEATURE_CONTROL_DATABASE_CLICKHOUSE_ENABLED=true \
-e FEATURE_CONTROL_DATABASE_MYSQL_ENABLED=true \
-e FEATURE_CONTROL_DATABASE_POSTGRES_ENABLED=true \
-e FEATURE_CONTROL_OSS_MINIO_ENABLED=true \
-e MINIO_ENDPOINT=http://${LOCAL_IP_ADDR}:9000 \
-e ATTACHMENT_SERVER_IP=${LOCAL_IP_ADDR} \
-e MYSQL_JDBC_URL=jdbc:mysql://${LOCAL_IP_ADDR}:3306/jt_808 \
-e MYSQL_JDBC_USERNAME=root \
-e MYSQL_JDBC_PASSWORD=mysql \
-e POSTGRES_JDBC_URL=jdbc:postgresql://${LOCAL_IP_ADDR}:5432/jt_808 \
-e POSTGRES_JDBC_USERNAME=jt \
-e POSTGRES_JDBC_PASSWORD=postgres \
-e CLICKHOUSE_JDBC_URL=jdbc:clickhouse://${LOCAL_IP_ADDR}:8123/jt_808 \
-e CLICKHOUSE_JDBC_USERNAME=jt \
-e CLICKHOUSE_JDBC_PASSWORD=clickhouse \
registry.cn-hangzhou.aliyuncs.com/xtream-codec/jt-808-server-quick-start-with-storage-blocking:latest
