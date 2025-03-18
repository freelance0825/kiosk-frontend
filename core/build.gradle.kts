plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.android.navigation)
}

apply(from = "../shared_dependencies.gradle")

android {
    namespace = "com.fmv.healthkiosk.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        buildConfigField("String", "APP_DATASTORE_NAME", "\"HEALTH_KIOSK_DATASTORE\"")
        buildConfigField("String", "APP_ENDPOINT", "\"http://10.0.2.2:8080/api/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
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
}

dependencies {

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.adapter.rxjava2)
}