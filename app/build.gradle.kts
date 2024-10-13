plugins {
    id("convention.android-app")
    id("kotlin-kapt")
}

android {
    namespace = "com.eakurnikov.trustore"

    defaultConfig {
        applicationId = "com.eakurnikov.trustore"
        versionName = "1.0"
        versionCode = 1
    }
}

dependencies {
    implementation(projects.trustoreApi)
    implementation(projects.trustoreImpl)
    implementation(projects.trustoreExt)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.dagger)
    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.kapt)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.test.junit)
}
