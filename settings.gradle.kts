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
