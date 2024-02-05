

import com.android.SdkConstants.FN_LOCAL_PROPERTIES
import com.android.build.gradle.internal.cxx.logging.infoln
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.sentry)
    alias(libs.plugins.apollo3)
    alias(libs.plugins.kotlin.serialization)
    id("com.codingfeline.buildkonfig")
    id("maven-publish")
    id("signing")
}


@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget()

    withSourcesJar(publish = false)

    listOf(
//        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            isStatic = true
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = libs.versions.libraryVersion.get()
        ios.deploymentTarget = libs.versions.ios.deploymentVersion.get()
        framework {
            baseName = "BotStacksSDK"
            export(libs.compose.adaptive.ui)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.runtimeSaveable)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.compose.adaptive.ui)
                implementation(libs.compose.adaptive.ui.filepicker)

                implementation(libs.compose.windowSizeClass)

                implementation(libs.apollo3.runtime)
                implementation(libs.apollo3.adapters)

                implementation(libs.coil3.compose)
                implementation(libs.coil3.gif)

                implementation(libs.coil3.network.ktor)

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.kermit)

                implementation(libs.ktor.client.core)

                implementation(libs.sentry)

                implementation(libs.settings)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.navigator.bottomsheet)
                implementation(libs.voyager.navigator.tabs)
                implementation(libs.voyager.transitions)
            }
        }

        androidMain {
            dependsOn(commonMain.get())
            dependencies {
                implementation(compose.preview)
                implementation(compose.uiTooling)

                implementation(libs.paho.mqtt.client)
                implementation(libs.apache.commons.text)
                implementation(libs.moshi.kotlin)
                implementation(libs.moshi.adapters)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core)
                implementation(libs.androidx.media3.exoplayer)
                implementation(libs.androidx.media3.ui)

                implementation(libs.android.colorx)

                implementation(libs.compose.markdown)
                implementation(libs.compose.permissions)

                implementation(libs.ktor.engine.android)

                implementation(libs.hive.mqtt.client)

                implementation(libs.contacts.async)
                implementation(libs.contacts.core)
                implementation(libs.contacts.vcard)

                implementation(libs.datafaker)
                implementation(libs.emoji.picker)
                implementation(libs.giphy)

                implementation(libs.google.play.services.location)

                implementation(firebaseLibs.firebaseMessagingKtx)

                implementation(libs.ok2curl)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.engine.darwin)
            }
        }
    }
}

android {
    namespace = "ai.botstacks.sdk"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    // loaded for android fonts
    sourceSets["main"].res.srcDirs("src/commonMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/io.netty.versions.properties",
                    "/META-INF/INDEX.LIST"
                )
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kotlin {
        jvmToolchain(17)
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "ai.botstacks"
            artifactId = "sdk"
            version = libs.versions.libraryVersion.get()

//            from(components.getByName("release"))

            pom {
                name.set("BotStacksChat")
                description.set("BotStacks Android Chat SDK")
                url.set("https://github.com/botstacks/android-example")

                licenses {
                    license {
                        name.set("Stream License")
                        url.set("https://github.com/botstacks/android-example/blob/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("reaperdtme")
                        name.set("reaper")
                        email.set("reaper@fastapps.one")
                    }
                }

                scm {
                    connection.set("scm:git:github.com/botstacks/android-sdl.git")
                    developerConnection.set("scm:git:ssh://github.com/botstacks/android-example.git")
                    url.set("https://github.com/botstacks/android-sdk/tree/main")
                }
            }
        }
    }
}

buildkonfig {
    packageName = "ai.botstacks.sdk"
    objectName = "SdkConfig"

    defaultConfigs {
        buildConfigField(BOOLEAN, "DEBUG", "true")
        buildConfigField(STRING, "ENV", "production")
        buildConfigField(STRING, "HOST", "chat.botstacks.ai")
        buildConfigField(BOOLEAN, "SSL", "true")
    }

    defaultConfigs("release") {
        buildConfigField(BOOLEAN, "DEBUG", "false")
        buildConfigField(STRING, "ENV", "production")
        buildConfigField(STRING, "HOST", "chat.botstacks.ai")
        buildConfigField(BOOLEAN, "SSL", "true")
    }
}

println("CHECK SIGNING")
if (rootProject.hasProperty("signing.keyId")) {
    println("Set Up Signing")
    signing {
        useInMemoryPgpKeys(
            gradleLocalProperties(rootProject.rootDir).getProperty("signing.keyId"),
            gradleLocalProperties(rootProject.rootDir).getProperty("signing.key"),
            gradleLocalProperties(rootProject.rootDir).getProperty("signing.password")
        )
        sign(publishing.publications)
    }
}

apollo {
    service("service") {
        packageName.set("ai.botstacks.sdk")
        mapScalar(
            "DateTime",
            "kotlinx.datetime.Instant",
            "com.apollographql.apollo3.adapter.KotlinxInstantAdapter"
        )
        mapScalarToKotlinDouble("Latitude")
        mapScalarToKotlinDouble("Longitude")
    }
}

/**
 * Retrieve the project local properties if they are available.
 * If there is no local properties file then an empty set of properties is returned.
 */
fun gradleLocalProperties(projectRootDir: File): Properties {
    val properties = Properties()
    val localProperties = File(projectRootDir, FN_LOCAL_PROPERTIES)
    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else {
        infoln("Gradle local properties file not found at $localProperties")
    }
    return properties
}