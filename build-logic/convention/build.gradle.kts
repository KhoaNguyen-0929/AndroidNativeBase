plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("android.library") {
            id = "focus.android.library"
            implementationClass = "buildlogic.AndroidLibraryConventionPlugin"
        }
        register("android.application") {
            id = "focus.android.application"
            implementationClass = "buildlogic.AndroidApplicationConventionPlugin"
        }
        register("kotlin.jvm") {
            id = "focus.kotlin.jvm"
            implementationClass = "buildlogic.KotlinJvmConventionPlugin"
        }
        register("compose") {
            id = "focus.compose"
            implementationClass = "buildlogic.ComposeConventionPlugin"
        }
        register("hilt") {
            id = "focus.hilt"
            implementationClass = "buildlogic.HiltConventionPlugin"
        }
        register("room") {
            id = "focus.room"
            implementationClass = "buildlogic.RoomConventionPlugin"
        }
        register("feature.serialization") {
            id = "focus.feature.serialization"
            implementationClass = "buildlogic.FeatureSerializationPlugin"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("com.android.tools.build:gradle:8.10.1")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.51")
}
