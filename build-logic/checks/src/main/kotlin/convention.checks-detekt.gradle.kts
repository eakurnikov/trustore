import com.eakurnikov.trustore.detektBaselinePath
import com.eakurnikov.trustore.detektConfigPath
import com.eakurnikov.trustore.ifNotDummy
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    id("io.gitlab.arturbosch.detekt")
}

tasks.register<Detekt>("detektAll") {
    description = "Runs over the entire code base without the starting overhead for each module and performs check."
    parallel = true
    buildUponDefaultConfig = true

    setSource(files(rootDir))
    config.setFrom(files(detektConfigPath))
    baseline.set(file(detektBaselinePath))

    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")

    reports {
        html.required.set(true)
        xml.required.set(false)
        md.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
    }
}

tasks.register<DetektCreateBaselineTask>("detektAllBaseline") {
    description = "Runs over the entire code base and overrides current baseline."
    parallel.set(true)
    ignoreFailures.set(true)
    buildUponDefaultConfig.set(true)

    setSource(files(rootDir))
    config.setFrom(files(detektConfigPath))
    baseline.set(file(detektBaselinePath))

    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
}

project.ifNotDummy {
    dependencies {
        val detektPlugins by configurations
        detektPlugins(libs.detekt.formatting)
    }
}
