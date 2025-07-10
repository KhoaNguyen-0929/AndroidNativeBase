plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    plugins {
        register("feature.serialization") {
            id = "focus.feature.serialization"
            implementationClass = "buildlogic.FeatureSerializationPlugin"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}
