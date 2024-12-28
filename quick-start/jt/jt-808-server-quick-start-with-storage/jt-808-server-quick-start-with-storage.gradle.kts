plugins {
    id("org.springframework.boot")
    application
}

application {
    mainClass.set("io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.Jt808ServerQuickStartWithStorageApp")
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

    // region r2dbc
    api("pro.chenggang:mybatis-r2dbc-spring:3.0.5.RELEASE")
    // mybatis-r2dbc-spring 的依赖
    api("org.apache.commons:commons-lang3")

    api("io.asyncer:r2dbc-mysql")
    api("org.postgresql:r2dbc-postgresql")
    api("com.clickhouse:clickhouse-client:0.7.1")
    api("com.clickhouse:clickhouse-r2dbc:0.7.1")
    // endregion r2dbc

    // 对象存储
    api("io.minio:minio:8.5.14")
}
