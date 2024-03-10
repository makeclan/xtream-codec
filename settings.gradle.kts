rootProject.name = "xtream-codec"
include("xtream-codec-core")
include("samples")
include("samples:xtream-codec-core-debug")

setBuildFileName(rootProject)

fun setBuildFileName(project: ProjectDescriptor) {
    project.children.forEach {
        it.buildFileName = "${it.name}.gradle.kts"

        assert(it.projectDir.isDirectory())
        assert(it.buildFile.isFile())

        setBuildFileName(it)
    }
}