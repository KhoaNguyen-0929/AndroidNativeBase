package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            // Note: Dependencies are handled by modules using libs.versions.toml
            // This plugin just applies the Compose compiler plugin
        }
    }
}
