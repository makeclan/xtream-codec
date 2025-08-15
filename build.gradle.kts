import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

plugins {
    id("java-library")
    id("io.spring.dependency-management")
    id("maven-publish")
    id("signing")
    id("checkstyle")
    id("com.github.joschi.licenser")
    id("com.github.jk1.dependency-license-report")
    id("com.namics.oss.gradle.license-enforce-plugin")
}

val mavenRepoConfig = getMavenRepoConfig()
val mavenPublications = setOf(
    "xtream-codec-base",
    "xtream-codec-core",
    "xtream-codec-server-reactive",
    "jt-808-server-spring-boot-starter-reactive",
    "jt-808-server-dashboard-spring-boot-starter-reactive",
)

// region Java
configure(subprojects) {
    if (!isJavaProject(project)) {
        return@configure
    }
    println("configure ....... " + project.name)

    apply(plugin = "java-library")
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

        dependencies {
            // 其他依赖版本都由上面的 mavenBom 控制
            // 这里指定 mavenBom 中没有包含的依赖版本
            dependency("io.github.classgraph:classgraph:4.8.174")
            dependency("org.bouncycastle:bcprov-jdk18on:1.78.1")
            dependency("pro.chenggang:mybatis-r2dbc-spring:3.0.5.RELEASE")
            dependency("cn.mybatis-mp:mybatis-mp-spring-boot-starter:1.7.8-spring-boot3")
            dependency("cn.mybatis-mp:mybatis-mp-datasource-routing:1.0.2")
            dependency("com.clickhouse:clickhouse-jdbc:0.7.2")
            dependency("com.clickhouse:clickhouse-client:0.7.1")
            dependency("com.clickhouse:clickhouse-r2dbc:0.7.1")
            dependency("com.taosdata.jdbc:taos-jdbcdriver:3.7.2")
            dependency("io.minio:minio:8.5.14")
            dependency("com.lmax:disruptor:4.0.0")
        }

        group = "xtream-codec"
        version = getProjectVersion()
    }

    repositories {
        extraMavenRepositoryUrls().forEach {
            maven(it)
        }
        mavenCentral()
        mavenLocal()
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }

    apply(plugin = "checkstyle")
    checkstyle {
        toolVersion = "10.23.0"
        configDirectory.set(rootProject.file("build-script/checkstyle/"))
    }
    tasks.withType<Checkstyle> {
        // 严重影响构建时间
        onlyIf {
            val skip = (project.findProperty("xtream.skip.checkstyle") as? String).toBoolean()
            if (skip && (project.findProperty("xtream.skip.logging.enabled") as? String).toBoolean()) {
                println("Disabling task: checkstyle in project [${project.name}](xtream.skip.checkstyle==true)")
            }
            return@onlyIf !skip
        }
    }

    // 本项目开源协议头
    apply(plugin = "com.github.joschi.licenser")
    license {
        header = rootProject.file("build-script/license/license-header")
        skipExistingHeaders = false
        exclude("**/spring.factories")
        exclude("**/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
    }

    apply(plugin = "com.github.jk1.dependency-license-report")
    // 第三方依赖 license
    licenseReport {
        // By default this plugin will collect the union of all licenses from
        // the immediate pom and the parent poms. If your legal team thinks this
        // is too liberal, you can restrict collected licenses to only include the
        // those found in the immediate pom file
        // Defaults to: true
        unionParentPomLicenses = true

        // Select projects to examine for dependencies.
        // Defaults to current project and all its subprojects
//        projects = project.subprojects.toTypedArray()
        projects = arrayOf(project)

        // Don't include artifacts of project's own group into the report
        excludeOwnGroup = true

        // Don't exclude bom dependencies.
        // If set to true, then all boms will be excluded from the report
        excludeBoms = true

        // excludes = mavenPublications.map { "xtream-codec:$it" }.toTypedArray()
        excludes = mavenPublications.flatMap { listOf("xtream-codec:$it", "xtream-codec.ext.jt:$it") }.toTypedArray()

        // Set output directory for the report data.
        // Defaults to ${project.buildDir}/reports/dependency-license.
        outputDir = "${project.layout.projectDirectory}/build/reports/dependency-license"

        // Set custom report renderer, implementing ReportRenderer.
        // Yes, you can write your own to support any format necessary.
        renderers = arrayOf(com.github.jk1.license.render.TextReportRenderer("THIRD-PARTY-NOTICES.txt"))

        // This is for the allowed-licenses-file in checkLicense Task
        // Accepts File, URL or String path to local or remote file
        ////// ??? https://github.com/jk1/Gradle-License-Report/issues/252
        allowedLicensesFile = rootProject.file("build-script/license/allowed-licenses.json")
    }

    apply(plugin = "com.namics.oss.gradle.license-enforce-plugin")
    tasks.enforceLicenses {
        allowedCategories = listOf("Apache", "MIT")
        allowedLicenses = listOf("Mulan Permissive Software License, Version 2")
    }
}
// endregion Java


// region Maven
configure(subprojects) {
    if (!isJavaProject(project)) {
        return@configure
    }
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    normalization {
        runtimeClasspath {
            ignore("META-INF/MANIFEST.MF")
        }
    }

    tasks.jar {
        dependsOn("generateLicenseReport")
        manifest {
            manifest.attributes["Implementation-Title"] = project.name
            manifest.attributes["Implementation-Version"] = getProjectVersion()
            manifest.attributes["Automatic-Module-Name"] = project.name.replace('-', '.')
            manifest.attributes["Created-By"] = "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"
            // manifest.attributes["Created-By"] = "${System.getProperty("java.version")} (${System.getProperty("java.vendor")})"
            manifest.attributes["Minimum-Jdk-Version"] = getJavaVersion()
        }

        from(rootProject.projectDir) {
            include("LICENSE")
            into("META-INF")
            rename("LICENSE", "LICENSE.txt")
            // https://docs.gradle.org/current/userguide/working_with_files.html#sec:filtering_files
            expand(
                "copyright" to LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")),
                "version" to getProjectVersion(),
            )
        }

        from(project.projectDir.absolutePath + "/build/reports/dependency-license/") {
            include("THIRD-PARTY-NOTICES.txt")
            into("META-INF")
            rename("THIRD-PARTY-NOTICES.txt", "NOTICE.txt")
        }
    }

    tasks.javadoc {
        description = "Generates project-level javadoc for use in -javadoc jar"
        options.encoding = "UTF-8"
        options.memberLevel = JavadocMemberLevel.PROTECTED
        options.header = project.name
        options.source = "21"

        val docletOptions = options as StandardJavadocDocletOptions
        docletOptions.addBooleanOption("html5", true)
        docletOptions.version(true)
        docletOptions.links("https://docs.oracle.com/en/java/javase/21/docs/api")
        docletOptions.charSet("UTF-8")
        docletOptions.use(true)
        docletOptions.addStringOption("Xdoclint:none", "-quiet")

        isFailOnError = false
        version = getProjectVersion()
        logging.captureStandardError(LogLevel.INFO)
        logging.captureStandardOutput(LogLevel.INFO)
    }

    val sourcesJar by tasks.registering(Jar::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier.set("sources")
        from(sourceSets.getByName("main").java.srcDirs)
    }

    val javaDocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks.named("javadoc"))
    }

    if (isMavenPublications(project)) {
        publishing {
            publications {
                create<MavenPublication>("maven") {

                    from(components["java"])
                    artifact(sourcesJar)
                    artifact(javaDocJar)

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
                            }
                        }

                        developers {
                            developer {
                                id.set(getConfigAsString("projectDeveloperId"))
                                name.set(getConfigAsString("projectDeveloperName"))
                                email.set(getConfigAsString("projectDeveloperEmail"))
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
                        // 1.1 将 build-script/maven/repo-credentials.debug-template.properties 另存到 ~/.gradle/repo-credentials.properties 然后修改用户名和密码等属性
                        // 1.2 在 ~/.gradle/gradle.properties 中配置 signing.keyId, signing.password, signing.secretKeyRingFile
                        maven {
                            name = "private"
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
                sign(publishing.publications["maven"])
            }
            ////// 在 ~/.gradle/gradle.properties 文件中配置:
            // 具体请参考模板文件: build-script/gradle/debug-template.gradle.properties
            // signing.keyId = ABCDEFGH
            // signing.password = you-password
            // signing.secretKeyRingFile = /path/to/secret.gpg
        }
    }

}
// endregion Maven

fun isJavaProject(project: Project): Boolean {
    return project != rootProject
            && (
            mavenPublications.contains(project.name)
                    || setOf(
                "xtream-codec-core-debug",
                "xtream-codec-server-reactive-debug-tcp",
                "xtream-codec-server-reactive-debug-udp",
                "jt-808-server-spring-boot-starter-reactive-debug",
                "jt-808-attachment-server-quick-start-blocking",
                "jt-808-attachment-server-quick-start-nonblocking",
                "jt-808-server-quick-start",
                "jt-808-server-quick-start-with-dashboard",
                "jt-808-server-quick-start-with-storage-nonblocking",
                "jt-808-server-quick-start-with-storage-blocking",
                "jt-1078-server-spring-boot-starter-reactive",
                "jt-1078-server-dashboard-spring-boot-starter-reactive",
                "jt-1078-server-spring-boot-starter-reactive-debug",
            ).contains(project.name))
}


fun isMavenPublications(project: Project): Boolean {
    return mavenPublications.contains(project.name)
}

fun needSign() = !rootProject.version.toString().lowercase().endsWith("snapshot")

fun getConfigAsString(key: String) = project.ext.get(key) as String

fun getConfigAsBoolean(key: String) = project.ext.get(key)?.toString()?.toBoolean() ?: false

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
        logger.quiet("The maven repository credentials file <<${fileName} -> {}>> not found , use `debug-template.repo-credentials.properties` for debugging.", repoCredentialFile.absolutePath)
        properties.load(rootProject.file("build-script/maven/debug-template.repo-credentials.properties").inputStream())
    }
    return properties
}

fun extraMavenRepositoryUrls(): List<String> {
    return listOf(
//        "https://mirrors.cloud.tencent.com/nexus/repository/maven-public",
//        "https://repo.huaweicloud.com/repository/maven",
        "https://maven.aliyun.com/repository/central",
        "https://maven.aliyun.com/repository/public",
        "https://maven.aliyun.com/repository/google",
        "https://maven.aliyun.com/repository/spring",
        // Central
        "https://repo1.maven.org/maven2",
        "https://maven.aliyun.com/repository/spring-plugin",
        "https://maven.aliyun.com/repository/gradle-plugin",
        "https://maven.aliyun.com/repository/grails-core",
        "https://maven.aliyun.com/repository/apache-snapshots",
        "https://plugins.gradle.org/m2/",
        "https://repo.spring.io/release",
        "https://repo.spring.io/snapshot"
    )
}
