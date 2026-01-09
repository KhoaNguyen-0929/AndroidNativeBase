package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            // Note: Dependencies are handled by modules using libs.versions.toml
            // This plugin is just a marker for modules that use Room
            // KSP will be applied separately via HiltConventionPlugin or directly

        }
    }
}
