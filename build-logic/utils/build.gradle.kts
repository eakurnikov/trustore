plugins {
    `kotlin-dsl`
}

dependencies {
    // Using workaround: https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
