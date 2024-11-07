dependencies {

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // common start
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // common end

    api(project(":xtream-codec-server-reactive"))
    api("com.github.ben-manes.caffeine:caffeine")
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-actuator")

    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.bouncycastle:bcprov-jdk18on")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
}
