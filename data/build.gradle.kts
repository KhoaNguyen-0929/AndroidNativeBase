plugins {
    id("focus.android.library")
    id("focus.hilt")
    id("focus.room")
}

android {
    namespace = "vn.start.data"

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions += "env"

    productFlavors {
        create("dev") {
            dimension = "env"
            buildConfigField("String", "SERVER_NORMAL_URL", "\"URL\"")
        }

        create("staging") {
            dimension = "env"
            buildConfigField("String", "SERVER_NORMAL_URL", "\"URL\"")
        }

        create("prod") {
            dimension = "env"
            buildConfigField("String", "SERVER_NORMAL_URL", "\"URL\"")
        }
    }
}

dependencies {
    // Core dependencies (from convention plugin)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Utilities
    implementation(libs.kotlinx.datetime)

    // Module dependencies
    implementation(project(":domain"))
    implementation(project(":core:common"))
}
