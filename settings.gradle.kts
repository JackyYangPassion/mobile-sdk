@file:Suppress("UnstableApiUsage")
import dev.aga.gradle.versioncatalogs.Generator.generate
import dev.aga.gradle.versioncatalogs.GeneratorConfig


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        maven("https://repo.eclipse.org/content/repositories/paho-snapshots/")
    }

    versionCatalogs {
        generate("firebaseLibs") {
            from(toml("firebase-bom"))
            aliasPrefixGenerator = GeneratorConfig.NO_PREFIX
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
    id("dev.aga.gradle.version-catalog-generator") version("1.1.0")
}

rootProject.name = "botstackschat"
include(":sdk")
include(":sample")
