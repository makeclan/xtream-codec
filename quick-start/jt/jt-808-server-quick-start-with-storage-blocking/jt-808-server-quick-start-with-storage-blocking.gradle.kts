plugins {
    id("org.springframework.boot")
    application
}

dependencies {
    // common start
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // common end

    // 加解密
    api("org.bouncycastle:bcprov-jdk18on")
    // api(project(":ext:jt:jt-808-server-spring-boot-starter-reactive"))
    api(project(":ext:jt:jt-808-server-dashboard-spring-boot-starter-reactive"))
    api("org.springframework.boot:spring-boot-starter-logging")

    implementation("jakarta.annotation:jakarta.annotation-api")

    // region jdbc
    api("cn.mybatis-mp:mybatis-mp-spring-boot-starter:1.7.8-spring-boot3")
    api("cn.mybatis-mp:mybatis-mp-datasource-routing:1.0.2")

    api("com.mysql:mysql-connector-j")
    api("org.postgresql:postgresql")
    api("com.clickhouse:clickhouse-jdbc:0.7.2")
    // endregion jdbc

    // 对象存储
    api("io.minio:minio:8.5.14")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")

}
