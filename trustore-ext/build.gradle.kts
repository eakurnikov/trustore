plugins {
    id("convention.kotlin-library")
    id("kotlin-kapt")
}

dependencies {
    implementation(projects.trustoreApi)

    implementation(libs.kotlin.coroutines.core)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}
