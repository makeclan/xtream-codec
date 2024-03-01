import java.util.*

plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.1"
    id("maven-publish")
    id("signing")
    id("checkstyle")
}

repositories {
    mavenLocal()
    extraMavenRepositoryUrls().forEach {
        maven(it)
    }
    mavenCentral()
}

val mavenRepoConfig = getMavenRepoConfig()

// region Java
configure(subprojects) {
    println("configure ....... " + project.name)

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.toVersion(getJavaVersion())
        targetCompatibility = JavaVersion.toVersion(getJavaVersion())
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
            mavenBom("org.springframework.boot:spring-boot-dependencies:${getConfigAsString("defaultSpringBootBomVersion")}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${getConfigAsString("defaultSpringCloudBomVersion")}")
        }
    }

    repositories {
        mavenLocal()
        extraMavenRepositoryUrls().forEach {
            maven(it)
        }
        mavenCentral()
    }

    tasks.test {
        useJUnitPlatform()
    }

    apply(plugin = "checkstyle")
    checkstyle {
        toolVersion = "10.9.1"
        configDirectory.set(rootProject.file("build-script/checkstyle/"))
    }
}
// endregion Java


// region Maven
configure(subprojects) {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    tasks.jar {
        manifest {
            manifest.attributes["Implementation-Title"] = project.name
            manifest.attributes["Implementation-Version"] = getProjectVersion()
            manifest.attributes["Automatic-Module-Name"] = project.name.replace('-', '.')
            manifest.attributes["JDK-Version"] = getJavaVersion()
            manifest.attributes["Created-By"] = System.getProperty("user.name")
        }

        from(rootProject.projectDir) {
            include("license.txt", "notice.txt")
            into("META-INF")
        }
    }

    tasks.javadoc {
        description = "Generates project-level javadoc for use in -javadoc jar"
        options.encoding = "UTF-8"
        options.memberLevel = JavadocMemberLevel.PROTECTED
        options.header = project.name
        version = getProjectVersion()
        logging.captureStandardError(LogLevel.INFO)
        logging.captureStandardOutput(LogLevel.INFO)
    }

    val sourcesJar by tasks.creating(Jar::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier.set("sources")
        from(sourceSets.getByName("main").java.srcDirs)
    }

    val javDocJar by tasks.creating(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks.named("javadoc"))
    }

    publishing {
        publications {
            create<MavenPublication>("mavenPublication") {

                from(components["java"])
                artifact(sourcesJar)
                artifact(javDocJar)

                groupId = getConfigAsString("projectGroupId")
                artifactId = project.name
                version = getProjectVersion()

                pom {
                    packaging = "jar"
                    description.set(project.name)
                    name.set(project.name)
                    url.set(getConfigAsString("projectHomePage"))

                    licenses {
                        license {
                            name.set(getConfigAsString("projectLicenseName"))
                            url.set(getConfigAsString("projectLicenseUrl"))
                            distribution.set(getConfigAsString("projectLicenseDistribution"))
                        }
                    }

                    versionMapping {
                        usage("java-api") {
                            fromResolutionOf("runtimeClasspath")
                        }
                        usage("java-runtime") {
                            fromResolutionResult()
                        }
                    }

                    scm {
                        url.set(getConfigAsString("projectScmUrl"))
                        connection.set(getConfigAsString("projectScmConnection"))
                        developerConnection.set(getConfigAsString("projectScmDeveloperConnection"))
                    }

                    issueManagement {
                        system.set(getConfigAsString("projectIssueManagementSystem"))
                        url.set(getConfigAsString("projectIssueManagementUrl"))
                    }
                }

                repositories {
                    // 1. 发布到你自己的私有仓库
                    // 1.1 在 ~/.gradle/repo-credentials.properties 中配置 privateRepo-release.url, privateRepo-release.username, privateRepo-release.password
                    // 1.2 在 ~/.gradle/gradle.properties 中配置 signing.keyId, signing.password, signing.secretKeyRingFile
                    maven {
                        name = "privateRepo"
                        url = uri(mavenRepoConfig.getProperty("privateRepo-release.url"))
                        credentials {
                            username = mavenRepoConfig.getProperty("privateRepo-release.username")
                            password = mavenRepoConfig.getProperty("privateRepo-release.password")
                        }
                    }

                    // 2. 发布到 Maven 中央仓库
                    maven {
                        name = "sonatype"
                        url = uri(mavenRepoConfig.getProperty("sonatype-staging.url"))
                        credentials {
                            username = mavenRepoConfig.getProperty("sonatype-staging.username")
                            password = mavenRepoConfig.getProperty("sonatype-staging.password")
                        }
                    }
                }
            }
        }

    }

    signing {
        if (needSign()) {
            sign(publishing.publications["mavenPublication"])
        }
        ////// 在 ~/.gradle/gradle.properties 文件中配置:
        // signing.keyId = ABCDEFGH
        // signing.password = you-password
        // signing.secretKeyRingFile = /path/to/secret.gpg
    }

}
// endregion Maven

fun needSign() = !rootProject.version.toString().lowercase().endsWith("snapshot")

fun getConfigAsString(key: String) = project.ext.get(key) as String

fun getProjectVersion() = getConfigAsString("projectVersion")

fun getJavaVersion() = getConfigAsString("defaultJavaVersion")

@JvmName("getMavenRepoConfigJvm")
fun getMavenRepoConfig(): Properties {
    val properties = Properties()
    val fileName = "repo-credentials.properties"
    val repoCredentialFile = file(System.getProperty("user.home") + "/.gradle/${fileName}")
    if (file(repoCredentialFile).exists()) {
        logger.quiet("The maven repository credentials file <<${fileName}>> will be load from: ${repoCredentialFile.absolutePath}")
        properties.load(repoCredentialFile.inputStream())
    } else {
        logger.quiet("The maven repository credentials file <<${fileName} -> {}>> not found , use `PLACEHOLDER` for debugging.", repoCredentialFile.absolutePath)
    }
    // populate with default values for debug purpose
    properties.putIfAbsent("privateRepo-release.url", "http:my-repo/repository/maven-release")
    properties.putIfAbsent("privateRepo-release.username", "iDoNotKnow1")
    properties.putIfAbsent("privateRepo-release.password", "iDoNotKnow2")

    properties.putIfAbsent("sonatype-staging.url", "https://oss.sonatype.org/service/local/staging/deploy/maven2")
    properties.putIfAbsent("sonatype-staging.username", "iDoNotKnow3")
    properties.putIfAbsent("sonatype-staging.password", "iDoNotKnow4")
    return properties
}

fun extraMavenRepositoryUrls(): List<String> {
    return listOf(
        "https://maven.aliyun.com/repository/central",
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
    )
}