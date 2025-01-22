FROM azul/zulu-openjdk:21
LABEL authors="hylexus"

WORKDIR /app/

ARG JAR_FILE=build/libs/jt-808-server-quick-start-with-storage-blocking-0.0.1-beta.12.jar
COPY ${JAR_FILE} app.jar

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
ENV APP_ARGS="--spring.profiles.active=docker_compose"

CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar ${APP_ARGS}"]

EXPOSE 8080
