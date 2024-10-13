plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(projects.kotlin)
    implementation(projects.checks)
    implementation(projects.utils)

    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.android.gradlePlugin)
    implementation(libs.android.cacheFix.gradlePlugin)

    // Using workaround: https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
