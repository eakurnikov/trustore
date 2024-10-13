plugins {
    id("convention.kotlin-multiplatform-library")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.trustoreApi)

                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.collections.immutable)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin)
                implementation(libs.test.kotlin.coroutines)
            }
        }
    }
}
