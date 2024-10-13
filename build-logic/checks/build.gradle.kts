plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(projects.utils)

    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.metalava.gradlePlugin)

    // Using workaround: https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
