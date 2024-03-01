import ai.botstacks.sdk.groupId
import ai.botstacks.sdk.versionName
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        // Add the dependency for the Google services Gradle plugin
        classpath(libs.gradlePlugin.android)
        classpath(libs.gradlePlugin.compose)
        classpath(libs.gradlePlugin.kotlin)
        classpath(libs.gradlePlugin.mavenPublish)
        classpath(libs.google.services)
        classpath(libs.buildkonfig)
    }
}
plugins {
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.dokka)
    alias(libs.plugins.nmcp).apply(false)
}


tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory = layout.projectDirectory.dir("docs/api")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    // Necessary to publish to Maven.
    group = groupId
    version = versionName

    tasks.withType<DokkaTaskPartial>().configureEach {
        dokkaSourceSets.configureEach {
            jdkVersion = 17
            failOnWarning = true
            skipDeprecated = true
            suppressInheritedMembers = true

            externalDocumentationLink(
                url = "https://developer.android.com/reference/",
            )
            externalDocumentationLink(
                url = "https://kotlinlang.org/api/kotlinx.coroutines/",
            )
            externalDocumentationLink(
                url = "https://square.github.io/okio/3.x/okio/",
                packageListUrl = "https://square.github.io/okio/3.x/okio/okio/package-list",
            )
            externalDocumentationLink(
                url = "https://jetbrains.github.io/skiko/",
                packageListUrl = "https://jetbrains.github.io/skiko/skiko/package-list",
            )
            externalDocumentationLink(
                url = "https://api.ktor.io/",
            )
        }
    }

    tasks.withType<DokkaTask>().configureEach {
        moduleName.set("BotStacks Chat SDK")
        moduleVersion.set(versionName)
        outputDirectory.set(project.file("../docs/api"))
        suppressObviousFunctions.set(true)
        suppressInheritedMembers.set(true)
        dokkaSourceSets.configureEach {
            documentedVisibilities.set(
                setOf(
                    DokkaConfiguration.Visibility.PUBLIC,
                    DokkaConfiguration.Visibility.PROTECTED,
                )
            )

            perPackageOption {
                matchingRegex.set(".*internal.*")
                suppress.set(true)
            }

            perPackageOption {
                matchingRegex.set("com.mikepenz.markdown.*")
                suppress.set(true)
            }

            perPackageOption {
                matchingRegex.set("com.mohamedrejeb.calf.*")
                suppress.set(true)
            }
        }
    }
}