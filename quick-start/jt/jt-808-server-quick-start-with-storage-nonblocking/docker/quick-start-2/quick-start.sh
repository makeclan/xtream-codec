#!/usr/bin/env sh

docker pull registry.cn-hangzhou.aliyuncs.com/xtream-codec/jt-808-server-quick-start-with-storage-nonblocking:latest

docker run --rm -p 8888:8888 \
-p 3927:3927 \
-p 3721:3721 \
-p 3824:3824 \
-p 3618:3618 \
-v ./application-demo.yaml:/app/application-demo.yaml \
registry.cn-hangzhou.aliyuncs.com/xtream-codec/jt-808-server-quick-start-with-storage-nonblocking:latest \
java \
-Djava.security.egd=file:/dev/./urandom \
-Dspring.config.additional-location=file:/app/application-demo.yaml \
-jar /app/app.jar \
--spring.profiles.active=demo
