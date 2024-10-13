package com.eakurnikov.trustore

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

/**
 * Using a workaround to provide an access to the version catalog for convention plugins:
 * https://github.com/gradle/gradle/issues/15383
 *
 * There are dummy projects created by gradle for type-safe accessors resolution,
 * and the version catalog is not accessible inside them. So we're checking
 * if we're inside one of these dummy project or not. If not we can use the version catalog.
 *
 * Note: the project that contains a convention plugin that uses this extension must have
 * implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
 * declaration in it's dependencies.
 */
fun Project.ifNotDummy(block: ProjectContext.() -> Unit) {
    if (name != "gradle-kotlin-dsl-accessors") {
        block.invoke(ProjectContext(this))
    }
}

class ProjectContext(project: Project) {
    val libs: LibrariesForLibs = project.the<LibrariesForLibs>()
}
