package ai.botstacks.sdk

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.setupPublishing(
    action: MavenPublishBaseExtension.() -> Unit = {},
) {
    extensions.configure<MavenPublishBaseExtension> {
        pomFromGradleProperties()
        publishToMavenCentral()
        signAllPublications()

        action()
    }

    extensions.configure<SigningExtension> {
        useGpgCmd()
    }
}

fun Project.androidLibrary(
    name: String,
    action: LibraryExtension.() -> Unit = {},
) = androidBase<LibraryExtension>(name) {
    buildFeatures {
        compose = true
        buildConfig = true
    }
    sourceSets["main"].resources {
        srcDirs("src/commonMain/resources", "src/jvmCommonMain/resources")
    }

    println("p=${project.name}")
    if (project.name in publicModules) {
        println("setting up publishing")
        apply(plugin = "org.jetbrains.dokka")
        apply(plugin = "com.vanniktech.maven.publish.base")
        apply(plugin = "signing")
        setupPublishing {
            val platform = if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                KotlinMultiplatform(JavadocJar.Dokka("dokkaGfm"))
            } else {
                AndroidSingleVariantLibrary()
            }
            configure(platform)
        }
    }
    action()
}

fun Project.androidApplication(
    name: String,
    action: BaseAppModuleExtension.() -> Unit = {},
) = androidBase<BaseAppModuleExtension>(name) {
    defaultConfig {
        applicationId = name
        versionCode = project.versionCode
        versionName = project.versionName
        resourceConfigurations += "en"
        vectorDrawables.useSupportLibrary = true
    }
    action()
}

private fun <T : BaseExtension> Project.androidBase(
    name: String,
    action: T.() -> Unit,
) {
    android<T> {
        namespace = name
        compileSdkVersion(project.compileSdkVersion)
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
        sourceSets["main"].res.srcDirs("src/commonMain/composeResources")

        defaultConfig {
            minSdk = this@androidBase.minSdkVersion
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")

            aarMetadata {
                minSdk = this@androidBase.minSdkVersion
            }
        }

        packagingOptions {
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

        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.configureEach {
                    languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
                    languageSettings.optIn("kotlinx.cinterop.BetaInteropApi")
                }
                targets.configureEach {
                    compilations.configureEach {
                        compilerOptions.configure {
                            val arguments = listOf(
                                // https://kotlinlang.org/docs/compiler-reference.html#progressive
                                "-progressive",
                                // https://youtrack.jetbrains.com/issue/KT-61573
                                "-Xexpect-actual-classes",
                            )
                            freeCompilerArgs.addAll(arguments)
                        }
                    }
                }
            }
        }
        action()
    }
}

private fun <T : BaseExtension> Project.android(action: T.() -> Unit) {
    extensions.configure("android", action)
}