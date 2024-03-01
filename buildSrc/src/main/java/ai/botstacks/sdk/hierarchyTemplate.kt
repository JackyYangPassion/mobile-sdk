package ai.botstacks.sdk

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

private val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {
        withCompilations { true }

        groupNonAndroid()
        groupNonJsCommon()
        groupJvmCommon()
        groupNative()
        groupNonNative()
    }
}

private fun KotlinHierarchyBuilder.groupNonAndroid() {
    group("nonAndroid") {
        withJvm()
        groupNative()
    }
}

private fun KotlinHierarchyBuilder.groupNonJsCommon() {
    group("nonJsCommon") {
        groupJvmCommon()
        groupNative()
    }
}

private fun KotlinHierarchyBuilder.groupJvmCommon() {
    group("jvmCommon") {
        withAndroidTarget()
        withJvm()
    }
}

private fun KotlinHierarchyBuilder.groupNative() {
    group("native") {
        withNative()

        group("apple") {
            withApple()

            group("ios") {
                withIos()
            }
        }
    }
}

private fun KotlinHierarchyBuilder.groupNonNative() {
    group("nonNative") {
        groupJvmCommon()
    }
}

fun KotlinMultiplatformExtension.applyBotStacksHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}