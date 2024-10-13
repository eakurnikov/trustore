import com.eakurnikov.trustore.ifNotDummy

plugins {
    kotlin
    id("convention.kotlin-base")
}

project.ifNotDummy {
    java {
        val javaVersion: JavaVersion = JavaVersion.toVersion(libs.versions.jvm.target.get())
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
