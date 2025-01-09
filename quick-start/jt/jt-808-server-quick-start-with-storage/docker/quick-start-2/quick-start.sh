#!/usr/bin/env sh

docker run --rm -p 8080:8080 \
-v ./application-demo.yaml:/app/application-demo.yaml \
registry.cn-hangzhou.aliyuncs.com/xtream-codec/jt-808-server-quick-start-with-storage:latest \
java \
-Djava.security.egd=file:/dev/./urandom \
-Dspring.config.additional-location=file:/app/application-demo.yaml \
-jar /app/app.jar \
--spring.profiles.active=demo
