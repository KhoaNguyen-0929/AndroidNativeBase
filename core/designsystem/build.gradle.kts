plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "vn.start.designsystem"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    lint {
        abortOnError = false
        checkDependencies = false
    }
}

// Disable lint tasks to avoid ComposeDetector crashes
tasks.matching { it.name.contains("lint") }.configureEach {
    enabled = false
}

dependencies {
    // Compose BOM for version alignment
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Core Compose dependencies (versions managed by BOM)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling)

    // Adaptive components
    implementation(libs.androidx.adaptive.android)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.coil.kt.compose)

    // Material Icons Extended (version managed by Compose BOM)
    implementation("androidx.compose.material:material-icons-extended")
}