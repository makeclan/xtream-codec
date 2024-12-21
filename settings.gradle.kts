pluginManagement {
    val defaultSpringBootBomVersion: String by settings
    plugins {
        id("io.spring.dependency-management") version "1.1.7" apply false
        id("org.springframework.boot") version defaultSpringBootBomVersion apply false
        id("com.github.joschi.licenser") version "0.6.0" apply false
        id("com.github.jk1.dependency-license-report") version "2.5" apply false
        id("com.namics.oss.gradle.license-enforce-plugin") version "1.7.0" apply false
    }

    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
            name = "aliyunGradlePlugin"
            content {
                // 404: https://maven.aliyun.com/repository/gradle-plugin/gradle/plugin/com/github/joschi/licenser/licenser/0.6.0/licenser-0.6.0.jar
                excludeGroup("gradle.plugin.com.github.joschi.licenser")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }

}

rootProject.name = "xtream-codec"
include("xtream-codec-core")
include("xtream-codec-server-reactive")

include("ext")
include("ext:jt")
include("ext:jt:jt-808-server-spring-boot-starter-reactive")
include("ext:jt:jt-808-server-dashboard-spring-boot-starter-reactive")

include("debug")
include("debug:xtream-codec-core-debug")
include("debug:xtream-codec-server-reactive-debug-tcp")
include("debug:xtream-codec-server-reactive-debug-udp")
include("debug:jt")
include("debug:jt:jt-808-server-spring-boot-starter-reactive-debug")

include("quick-start")
include("quick-start:jt")
include("quick-start:jt:jt-808-server-quick-start")
include("quick-start:jt:jt-808-server-quick-start-with-dashboard")

setBuildFileName(rootProject)

fun setBuildFileName(project: ProjectDescriptor) {
    project.children.forEach {
        it.buildFileName = "${it.name}.gradle.kts"

        assert(it.projectDir.isDirectory())
        assert(it.buildFile.isFile())

        setBuildFileName(it)
    }
}
