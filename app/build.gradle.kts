plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.android.navigation)
}

apply(from = "../shared_dependencies.gradle")

android {
    packaging {
        resources {
            excludes += setOf(
                "proguard.txt",
                "AndroidManifest.xml",
                "R.txt"
            )
        }
    }

    namespace = "com.fmv.healthkiosk"
    compileSdk = 35

    packaging {
        resources {
            excludes += setOf("resources.arsc", "AndroidManifest.xml")
        }
    }

    defaultConfig {
        applicationId = "com.fmv.healthkiosk"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
    implementation(project(":core"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:tests"))
    implementation(project(":feature:telemedicine"))

    implementation(libs.material)
    implementation(libs.constraintlayout)

    // JitsiMeet
    implementation("org.jitsi.react:jitsi-meet-sdk:11.1.0") {
        isTransitive = true
    }

    // ThreeTenABP for backward-compatible date/time handling
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.5")

    // Bluetooth
    implementation(fileTree(mapOf("dir" to "../libs", "include" to listOf("*.jar"))))
    implementation(fileTree(mapOf("dir" to "../libs", "include" to listOf("*.aar"))))
    implementation(libs.appcompat)
    implementation(libs.activity)
}