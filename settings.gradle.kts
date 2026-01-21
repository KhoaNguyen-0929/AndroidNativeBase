pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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
include(":core:common")
include(":domain")
include(":data")
include(":core:ui")
include(":core:notifications")
include(":core:designsystem")
include(":features:home")
include(":features:pomodoro")
include(":features:tasks")
include(":features:stats")
include(":features:focus")
include(":features:dashboard")
include(":features:planning")
includeBuild("build-logic")
include(":core:datastore")
include(":core:navigation")
