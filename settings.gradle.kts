pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidNativeBase"
include(":app")
include(":domain")
include(":data")
include(":features:focus")
include(":features:planning")
include(":features:dashboard")
include(":core:model")
include(":core:common")
include(":core:designsystem")
include(":core:notifications")
include(":core:ui")