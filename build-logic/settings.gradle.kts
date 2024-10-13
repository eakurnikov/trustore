rootProject.name = "build-logic"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-logic-settings")
}

plugins {
    id("convention.repositories")
}

include("android")
include("kotlin")
include("checks")
include("utils")
