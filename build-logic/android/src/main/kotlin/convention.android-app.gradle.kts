import com.eakurnikov.trustore.ifNotDummy

plugins {
    id("com.android.application")
    `kotlin-android`
    `kotlin-composecompiler`
    id("convention.kotlin-base")
    id("convention.android-base")
}

android {
    project.ifNotDummy {
        defaultConfig {
            targetSdk = libs.versions.android.targetSdk.get().toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    sourceSets {
        getByName("debug") {
            java.srcDir("src/debug/kotlin")
        }
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures.compose = true
}
