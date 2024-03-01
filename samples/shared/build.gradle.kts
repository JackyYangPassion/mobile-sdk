import ai.botstacks.sdk.compileSdkVersion
import ai.botstacks.sdk.minSdkVersion

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.chatSdk)
            }
        }
    }
}

android {
    namespace = "ai.botstacks.sample.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }
}