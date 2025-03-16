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
    api(project(":ext:jt:jt-1078-server-spring-boot-starter-reactive"))
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-logging")

    implementation("jakarta.annotation:jakarta.annotation-api")

}
