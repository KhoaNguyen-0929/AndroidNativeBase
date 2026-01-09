plugins {
    id("focus.android.library")
    id("focus.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "vn.start.focus"
}

dependencies {
    // Core dependencies (from convention plugin)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose (from convention plugin)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.material3.adaptive.navigation)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
}
