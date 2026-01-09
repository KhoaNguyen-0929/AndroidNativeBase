package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class FeatureSerializationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            // Note: Dependencies are handled by modules using libs.versions.toml
            // This plugin just applies the Kotlin serialization plugin
        }
    }
}
