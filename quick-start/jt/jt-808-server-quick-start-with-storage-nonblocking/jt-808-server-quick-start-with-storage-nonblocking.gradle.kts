import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    id("org.springframework.boot")
    application
}

application {
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
    api("pro.chenggang:mybatis-r2dbc-spring")
    // mybatis-r2dbc-spring 的依赖
    api("org.apache.commons:commons-lang3")

    api("io.asyncer:r2dbc-mysql")
    api("org.postgresql:r2dbc-postgresql")
    api("com.clickhouse:clickhouse-client")
    api("com.clickhouse:clickhouse-r2dbc")
    // endregion r2dbc

    // 对象存储
    api("io.minio:minio")
}

val quickStartUiStaticDir = project.file("src/main/resources/static")
// 前端打包生成的文件 不检测 License
extensions.configure(LicenseExtension::class.java) {
    exclude {
        it.file.startsWith(quickStartUiStaticDir)
    }
}

// ./gradlew clean build -P buildQuickstartUiNonblocking=true
val buildQuickstartUiNonblocking = getConfigAsBoolean("buildQuickstartUiNonblocking") || project.findProperty("buildQuickstartUiNonblocking") == "true"
val quickstartUiDir = file("../jt-808-server-quick-start-with-storage-ui")
val quickstartUiGroup = "quickstart"

tasks.register<Exec>("buildQuickstartUiNonblocking") {
    onlyIf { buildQuickstartUiNonblocking }
    group = quickstartUiGroup
    description = "构建 quickstart-ui"
    workingDir = file(quickstartUiDir)
    commandLine(
        "sh", "-c",
        """
        echo "===> 开始构建 quickstart-ui"
        pnpm install --registry https://registry.npmmirror.com
        pnpm run build
        echo "===> 构建 quickstart-ui 成功"
        """.trimIndent()
    )
}

tasks.register<Copy>("copyQuickstartUiDistNonblocking") {
    onlyIf { buildQuickstartUiNonblocking }
    group = quickstartUiGroup
    description = "复制 quickstart-ui 构建输出"
    from("${quickstartUiDir}/dist")
    into("src/main/resources/static/quickstart-ui/")
    include("**/*")

    doFirst {
        delete("src/main/resources/static/quickstart-ui/")
    }

    // 始终重新执行任务
    outputs.upToDateWhen { false }
}

tasks.named("processResources").configure {
    if (buildQuickstartUiNonblocking) {
        dependsOn(tasks.named("copyQuickstartUiDistNonblocking"))
    }
}

tasks.named("build").configure {
    if (buildQuickstartUiNonblocking) {
        dependsOn(tasks.named("buildQuickstartUiNonblocking"))
        dependsOn(tasks.named("copyQuickstartUiDistNonblocking"))
    }
}

fun getConfigAsBoolean(key: String) = project.ext.get(key)?.toString()?.toBoolean() ?: false
