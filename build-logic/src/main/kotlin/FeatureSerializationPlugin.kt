package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.pluginManager

class FeatureSerializationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        project.dependencies {
            add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        }
    }
}
