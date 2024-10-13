package com.eakurnikov.trustore

import org.gradle.api.Project

val Project.checksConfigsPath: String
    get() = "$rootDir/build-logic/checks/configs"

val Project.detektConfigPath: String
    get() = "$checksConfigsPath/detekt/config.yml"

val Project.detektBaselinePath: String
    get() = "$checksConfigsPath/detekt/baseline.xml"
