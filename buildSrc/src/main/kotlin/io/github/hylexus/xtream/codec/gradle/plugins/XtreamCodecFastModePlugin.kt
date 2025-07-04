package io.github.hylexus.xtream.codec.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 由于本项目中示例性的 spring-boot FatJar 模块很多(严重影响构建时间)
 *
 * 因此，在 fastMode == true 时，禁用一些任务以加快构建速度
 *
 * @author hylexus
 */
class XtreamCodecFastModePlugin : Plugin<Project> {

    // 这几个任务严重影响构建时间
    private val disabledTasks = setOf(
        "bootJar",
        "bootDistZip",
        "bootDistTar",
        "distZip",
        "distTar",
    )

    override fun apply(project: Project) {
        val fastMode = project.findProperty("xtream.skip.fatjar")?.toString()?.toBoolean() ?: false
        val fastModeLogging = project.findProperty("xtream.skip.logging.enabled")?.toString()?.toBoolean() ?: false
        val disabledTaskNames = kotlin.collections.HashSet<String>()
        if (fastMode) {
            disabledTasks.forEach { taskName ->
                project.tasks.findByName(taskName)?.let { task ->
                    task.enabled = false
                    disabledTaskNames.add(taskName)
                }
            }
            if (fastModeLogging) {
                println("Disabling task: $disabledTaskNames in project [${project.name}](xtream.skip.fatjar==true)")
            }
        }
    }

}
