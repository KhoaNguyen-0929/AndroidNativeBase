plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "vn.start.data"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(project(":domain"))
    implementation(project(":core:common"))
}