import me.tylerbwong.gradle.metalava.extension.MetalavaExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

afterEvaluate {
    if (shouldApplyMetalava) {
        apply(plugin = "me.tylerbwong.gradle.metalava")

        extensions.configure<MetalavaExtension> {
            sourcePaths.setFrom(
                kotlinExtension.sourceSets.filterPublicApi().map { "src/${it.name}" }
            )
        }
    } else {
        logger.log(LogLevel.LIFECYCLE, "Metalava: skipping $path - not a public API")
    }
}

private val publicApiExclusions: List<String> = arrayListOf(":app")

private val Project.shouldApplyMetalava: Boolean
    get() = !hasProperty("skipMetalava") &&
        !path.startsWithAnyOf(publicApiExclusions)

private fun Iterable<KotlinSourceSet>.filterPublicApi(): List<KotlinSourceSet> {
    return filter { sourceSet: KotlinSourceSet ->
        val sourceSetName: String = sourceSet.name.lowercase()
        !sourceSetName.contains("test") && !sourceSetName.contains("sample")
    }
}

private fun String.startsWithAnyOf(prefixes: List<String>, ignoreCase: Boolean = false): Boolean {
    return prefixes.any { startsWith(it, ignoreCase) }
}
