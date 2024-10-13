enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Trustore"

pluginManagement {
    includeBuild("build-logic-settings")
    includeBuild("build-logic")
}

plugins {
    id("convention.repositories")
    id("convention.scan")
}

include(":app")
include(":trustore-api")
include(":trustore-impl")
include(":trustore-ext")
