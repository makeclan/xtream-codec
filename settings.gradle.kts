rootProject.name = "xtream-codec"
include("xtream-codec-core")
include("xtream-codec-server-reactive")

include("ext")
include("ext:jtt")
include("ext:jtt:xtream-codec-ext-jt-808-server-spring-boot-starter")

include("samples")
include("samples:xtream-codec-core-debug")
include("samples:xtream-codec-server-reactive-debug-tcp")
include("samples:xtream-codec-server-reactive-debug-udp")
include("samples:jtt")
include("samples:jtt:jt-808-server-spring-boot-starter-debug")

include("quick-start")
include("quick-start:jt")
include("quick-start:jt:jt-808-server-quick-start")

setBuildFileName(rootProject)

fun setBuildFileName(project: ProjectDescriptor) {
    project.children.forEach {
        it.buildFileName = "${it.name}.gradle.kts"

        assert(it.projectDir.isDirectory())
        assert(it.buildFile.isFile())

        setBuildFileName(it)
    }
}
