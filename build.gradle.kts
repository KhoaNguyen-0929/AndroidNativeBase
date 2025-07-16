// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}

allprojects {
    configurations.all {
        resolutionStrategy {
            // Force Kotlin 1.9.23
            force("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.23")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23")
            force("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.23")
            force("org.jetbrains.kotlin:kotlin-reflect:1.9.23")

            // Force coroutines 1.7.3
            force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            force("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
            force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
        }
    }
}