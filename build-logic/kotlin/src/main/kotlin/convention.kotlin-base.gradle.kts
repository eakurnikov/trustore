import com.eakurnikov.trustore.ifNotDummy
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("convention.checks-metalava")
}

project.ifNotDummy {
    val javaVersion: String = libs.versions.jvm.target.get()

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(javaVersion)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
