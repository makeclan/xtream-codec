import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    id("org.springframework.boot")
    application
}

application {
    mainClass.set("io.github.hylexus.xtream.debug.ext.jt808.Jt808SpringBootStarterDebugApp")
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
//    api(project(":ext:jt:jt-808-server-spring-boot-starter-reactive"))
    api(project(":ext:jt:jt-808-server-dashboard-spring-boot-starter-reactive"))
    api("io.projectreactor:reactor-core-micrometer")
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-logging")

    implementation("jakarta.annotation:jakarta.annotation-api")

}

val debugUiStaticDir = project.file("src/main/resources/static")
// 前端打包生成的文件 不检测 License
extensions.configure(LicenseExtension::class.java) {
    exclude {
        it.file.startsWith(debugUiStaticDir)
    }
}

// ./gradlew clean build -P buildJt808DebugUi=true
val buildDebugUi = project.findProperty("buildJt808DebugUi") == "true"
val debugUiDir = file("../jt-808-server-spring-boot-starter-debug-ui")
val debugUiGroup = "jt808-debug-ui"

tasks.register<Exec>("buildJt808DebugUi") {
    onlyIf { buildDebugUi }
    group = debugUiGroup
    description = "构建jt808-debug-ui"
    workingDir = file(debugUiDir)
    commandLine("pnpm", "install", "--registry", "https://registry.npmmirror.com")
    commandLine("pnpm", "run", "build")
}

tasks.register<Copy>("copyJt808DebugUiDist") {
    onlyIf { buildDebugUi }
    group = debugUiGroup
    description = "复制jt808-debug-ui构建输出"
    from("${debugUiDir}/dist")
    into("src/main/resources/static")
    include("**/*")
}

tasks.named("processResources").configure {
    if (buildDebugUi) {
        dependsOn(tasks.named("copyJt808DebugUiDist"))
    }
}

tasks.named("build").configure {
    if (buildDebugUi) {
        dependsOn(tasks.named("buildJt808DebugUi"))
        dependsOn(tasks.named("copyJt808DebugUiDist"))
    }
}
