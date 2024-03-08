import ai.botstacks.sdk.addAllMultiplatformTargets
import ai.botstacks.sdk.androidLibrary
import ai.botstacks.sdk.iosDeploymentTarget
import ai.botstacks.sdk.versionName
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import dev.icerock.gradle.MRVisibility

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    kotlin("native.cocoapods")
    id("org.jetbrains.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.apollo3)
    id("com.codingfeline.buildkonfig")
    alias(libs.plugins.dokka)
    alias(libs.plugins.nmcp)
    id("dev.icerock.mobile.multiplatform-resources")
}

addAllMultiplatformTargets()
androidLibrary(name = "ai.botstacks.sdk") {
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    kotlin {
        jvmToolchain(17)
    }

    buildTypes {
        create("dev") {
            initWith(getByName("debug"))
        }
    }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    cocoapods {
        name = "BotStacks_ChatSDK"
        summary = "BotStacks Core Chat SDK"
        homepage = "https://botstacks.ai"
        license = "MIT"
        authors = "BotStacks"
        version = versionName
        ios.deploymentTarget = iosDeploymentTarget

        framework {
            baseName = "BotStacks_ChatSDK"
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
//                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                api(libs.moko.resources)
                implementation(libs.moko.resources.compose)

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
                implementation(compose.uiTooling)

                implementation(libs.apache.commons.text)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core)
                implementation(libs.androidx.media3.exoplayer)
                implementation(libs.androidx.media3.ui)

                implementation(libs.compose.permissions)

                implementation(libs.coil3.gif)

                implementation(libs.ktor.engine.android)

                implementation(libs.contacts.vcard)

                implementation(libs.google.maps.compose)
                implementation(libs.google.maps.compose.utils)

                implementation(libs.datafaker)
                implementation(libs.giphy)

                implementation(libs.google.play.services.location)

                implementation(firebaseLibs.firebaseMessagingKtx)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.engine.darwin)
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "ai.botstacks.`chat-sdk`.generated.resources"
    multiplatformResourcesClassName = "Res"
    multiplatformResourcesVisibility = MRVisibility.Internal
    iosBaseLocalizationRegion = "en"
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

    defaultConfigs("dev") {
        buildConfigField(BOOLEAN, "DEBUG", "true")
        buildConfigField(STRING, "ENV", "dev")
        buildConfigField(STRING, "HOST", "chat.dev.botstacks.ai")
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
        // don't include apollo sources in output frameworks
        generateAsInternal.set(true)
    }
}

project.afterEvaluate {
    tasks.named("androidReleaseSourcesJar") {
        dependsOn("generateMRcommonMain")
        dependsOn("generateMRandroidMain")
    }

    tasks.named("dokkaGfm") {
        dependsOn("generateMRiosArm64Main")
        dependsOn("generateMRiosSimulatorArm64Main")
        dependsOn("generateMRiosX64Main")
    }

    tasks.named("iosArm64SourcesJar") {
        dependsOn("generateMRcommonMain")
        dependsOn("generateMRiosArm64Main")
    }

    tasks.named("iosSimulatorArm64SourcesJar") {
        dependsOn("generateMRcommonMain")
        dependsOn("generateMRiosSimulatorArm64Main")
    }

    tasks.named("iosX64SourcesJar") {
        dependsOn("generateMRcommonMain")
        dependsOn("generateMRiosX64Main")
    }

    tasks.named("sourcesJar") {
        dependsOn("generateMRcommonMain")
    }
}

tasks.register("copyBotStacksResourcesToApp") {
    dependsOn("copyFrameworkResourcesToApp")
}

nmcp {
    publishAllPublications {
        username = tryReadProperty("mavenCentralUsername")
        password = tryReadProperty("mavenCentralPassword")
        publicationType = if (versionName.endsWith("SNAPSHOT")) "AUTOMATIC" else "USER_MANAGED"
    }
}