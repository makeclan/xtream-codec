plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.1"
}

repositories {
    mavenLocal()
    arrayOf("https://maven.aliyun.com/repository/central",
            "https://maven.aliyun.com/repository/public",
            "https://maven.aliyun.com/repository/google",
            "https://maven.aliyun.com/repository/spring",
            "https://maven.aliyun.com/repository/spring-plugin",
            "https://maven.aliyun.com/repository/gradle-plugin",
            "https://maven.aliyun.com/repository/grails-core",
            "https://maven.aliyun.com/repository/apache-snapshots",
            "https://plugins.gradle.org/m2/",
            "https://repo.spring.io/release",
            "https://repo.spring.io/snapshot"
    ).forEach {
        maven(it)
    }
    mavenCentral()
}

configure(subprojects) {
    println("configure ....... " + project.name)

    group = "io.github.hylexus.xtream"
    version = "0.0-SNAPSHOT"

    apply(plugin = ("java"))
    apply(plugin = ("io.spring.dependency-management"))

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencyManagement {
        resolutionStrategy {
            cacheChangingModulesFor(0, TimeUnit.SECONDS)
        }
        applyMavenExclusions(false)
        generatedPomCustomization {
            enabled(false)
        }
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.1")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
        }
    }

    repositories {
        mavenLocal()
        arrayOf("https://maven.aliyun.com/repository/central",
                "https://maven.aliyun.com/repository/public",
                "https://maven.aliyun.com/repository/google",
                "https://maven.aliyun.com/repository/spring",
                "https://maven.aliyun.com/repository/spring-plugin",
                "https://maven.aliyun.com/repository/gradle-plugin",
                "https://maven.aliyun.com/repository/grails-core",
                "https://maven.aliyun.com/repository/apache-snapshots",
                "https://plugins.gradle.org/m2/",
                "https://repo.spring.io/release",
                "https://repo.spring.io/snapshot"
        ).forEach {
            maven(it)
        }
        mavenCentral()
    }

    tasks.test {
        useJUnitPlatform()
    }
}

