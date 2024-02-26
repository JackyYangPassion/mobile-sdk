import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.apollo3)
    id("com.codingfeline.buildkonfig")
    id("co.touchlab.kmmbridge") version "0.5.2"
    id("org.jetbrains.dokka") version "1.9.10"
    id("maven-publish")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    cocoapods {
        name = "botstacks-core"
        summary = "Internal BotStacks Core KMP SDK"
        homepage = "https://botstacks.ai"
        license = "MIT"
        authors = "BotStacks"
        version = libs.versions.libraryVersion.get()
        ios.deploymentTarget = libs.versions.ios.deploymentVersion.get()

        publishDir = project.file("../pod")

        framework {
            baseName = "botstacks-core"
            isStatic = true
        }

        pod("Giphy") {
            moduleName = "GiphyUISDK"
            version = "2.2.8"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("GoogleMaps") {
            version = "8.4.0"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes"
        )
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("kotlinx.cinterop.BetaInteropApi")
        }
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
                implementation(compose.components.resources)

                implementation(libs.compose.windowSizeClass)

                implementation(libs.apollo3.runtime)
                implementation(libs.apollo3.adapters)

                implementation(libs.coil3.compose)

                implementation(libs.coil3.network.ktor)

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.ktor.client.core)

                implementation(libs.markdown)

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

                implementation(libs.compose.permissions)

                implementation(libs.coil3.gif)

                implementation(libs.ktor.engine.android)

                implementation(libs.hive.mqtt.client)

                implementation(libs.contacts.async)
                implementation(libs.contacts.core)
                implementation(libs.contacts.vcard)

                implementation(libs.google.maps.compose)
                implementation(libs.google.maps.compose.utils)

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
    namespace = "${(findProperty("project.namespace") as String)}.sdk"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/commonMain/composeResources")

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

tasks.named("podPublishDebugXCFramework") {
    doLast {
        copy {
            from(project.file("src/commonMain/composeResources"))
            into(project.file("../pod/debug/build/compose/ios/botstacks-core/compose-resources"))
        }
    }
}

tasks.named("podPublishReleaseXCFramework") {
    doLast {
        copy {
            from(project.file("src/commonMain/composeResources"))
            into(project.file("../pod/release/build/compose/ios/botstacks-core/compose-resources"))
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set("BotStacksSDK")
    moduleVersion.set(libs.versions.libraryVersion.get())
    outputDirectory.set(project.file("../docs/api"))
    suppressObviousFunctions.set(true)
    suppressInheritedMembers.set(true)
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(
            setOf(
                Visibility.PUBLIC,
                Visibility.PROTECTED,
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

kmmbridge {
//    mavenPublishArtifacts()
}

dependencies {
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.10")
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
                description.set("BotStacks Multiplatform Mobile SDK")
                url.set("https://github.com/botstacks/mobile-sdk")

                licenses {
                    license {
                        name.set("Stream License")
                        url.set("https://github.com/botstacks/android-example/blob/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("bmc08gt")
                        name.set("Brandon McAnsh")
                        email.set("brandon@bmcreations.dev")
                    }
                }

                scm {
                    connection.set("scm:git:github.com/botstacks/mobile-sdk.git")
                    developerConnection.set("scm:git:ssh://github.com/botstacks/mobile-sdk.git")
                    url.set("https://github.com/botstacks/mobile-sdk/tree/main")
                }
            }
        }
    }
}