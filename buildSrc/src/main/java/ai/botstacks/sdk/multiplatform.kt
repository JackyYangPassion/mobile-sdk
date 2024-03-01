package ai.botstacks.sdk

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.gradle.kotlin.dsl.configure

fun Project.addAllMultiplatformTargets() {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            applyBotStacksHierarchyTemplate()

            val isAndroidApp = plugins.hasPlugin("com.android.application")
            val isAndroidLibrary = plugins.hasPlugin("com.android.library")
            if (isAndroidApp || isAndroidLibrary) {
                androidTarget {
                    if (isAndroidLibrary) {
                        publishLibraryVariants("release")
                    }
                }
            }

            iosX64()
            iosArm64()
            iosSimulatorArm64()
        }
    }
}