dependencies {
    // common start
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // common end

    api(project(":xtream-codec-core"))
    api("io.netty:netty-buffer")
    api("io.projectreactor.netty:reactor-netty-core")
    api("org.springframework.boot:spring-boot-starter-logging"){
        exclude(group = "org.apache.logging.log4j")
        exclude(module = "jul-to-slf4j")
    }

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("io.projectreactor:reactor-core-micrometer")

}
