package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            apply(plugin = "com.google.dagger.hilt.android")
            apply(plugin = "com.google.devtools.ksp")

            // Note: Dependencies are handled by modules using libs.versions.toml
            // This plugin just applies the Hilt and KSP plugins
        }
    }
}
