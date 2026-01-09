plugins {
    id("focus.kotlin.jvm")
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Hilt (core only for JVM modules)
    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    // Module dependencies
    implementation(project(":core:common"))
}
