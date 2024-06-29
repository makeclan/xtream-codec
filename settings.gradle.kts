rootProject.name = "xtream-codec"
include("xtream-codec-core")
include("xtream-codec-server-reactive")
include("samples")
include("samples:xtream-codec-core-debug")
include("samples:xtream-codec-server-reactive-debug-tcp")
include("samples:xtream-codec-server-reactive-debug-udp")

setBuildFileName(rootProject)

fun setBuildFileName(project: ProjectDescriptor) {
    project.children.forEach {
        it.buildFileName = "${it.name}.gradle.kts"

        assert(it.projectDir.isDirectory())
        assert(it.buildFile.isFile())

        setBuildFileName(it)
    }
}
