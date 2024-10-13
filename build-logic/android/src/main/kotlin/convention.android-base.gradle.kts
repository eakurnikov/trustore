import com.android.build.gradle.BaseExtension
import com.eakurnikov.trustore.ifNotDummy

plugins {
    id("org.gradle.android.cache-fix")
}

configure<BaseExtension> {
    project.ifNotDummy {
        compileSdkVersion(libs.versions.android.compileSdk.get().toInt())
        defaultConfig.minSdk = libs.versions.android.minSdk.get().toInt()

        compileOptions {
            val javaVersion: JavaVersion = JavaVersion.toVersion(libs.versions.jvm.target.get())
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }
}
