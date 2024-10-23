plugins {
    id("convention.kotlin-library")
}

dependencies {
    implementation(projects.trustoreApi)

    implementation(libs.kotlin.coroutines.core)
}
