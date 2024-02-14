import org.jetbrains.kotlin.tooling.core.withClosure
import java.io.FileInputStream
import java.util.Properties

buildscript {
    repositories {
        // Make sure that you have the following two repositories
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central repository
    }

    dependencies {
        // Add the dependency for the Google services Gradle plugin
        classpath(libs.google.services)
        classpath(libs.buildkonfig)
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false) version libs.versions.kotlin.get()
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.gradle.nexus.publish)
}

// Create variables with empty default values
val secretPropsFile = file("local.properties")
if (secretPropsFile.exists()) {
    // Read local.properties file first if it exists
    val p = Properties()
    FileInputStream(secretPropsFile).use { p.load(it) }

    p.forEach { name, value ->
        ext[name.toString()] = value
    }
} else {
    // Use system environment variables
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    ext["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.key"] = System.getenv("SIGNING_KEY")
}
