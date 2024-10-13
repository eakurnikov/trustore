plugins {
    id("com.gradle.develocity")
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        uploadInBackground = true
        publishing.onlyIf {
            providers.gradleProperty("buildScan").orNull.toBoolean()
        }
    }
}
