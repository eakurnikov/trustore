plugins {
    id("convention.kotlin-library")
}

dependencies {
    implementation(projects.trustoreApi)
    implementation(projects.trustoreImpl)

    implementation(libs.kotlin.coroutines.core)
}
