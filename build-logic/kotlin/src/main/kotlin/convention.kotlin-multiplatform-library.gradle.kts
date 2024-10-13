plugins {
    id("kotlin-multiplatform")
    id("convention.kotlin-base")
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting
        val commonTest by getting
    }
}
