import com.android.SdkConstants.FN_LOCAL_PROPERTIES
import com.android.build.gradle.internal.cxx.logging.infoln
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
//    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    kotlin("kapt")
    alias(libs.plugins.sentry)
    alias(libs.plugins.apollo3)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
    id("signing")
}

val libraryVersion = "1.0.0"

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget()

    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.runtimeSaveable)
                api(compose.foundation)
                api(compose.material)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api(compose.ui)
                api(compose.uiTooling)
                api(compose.preview)
                api(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                api(compose.components.resources)

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.retrofit2)
                implementation(libs.retrofit2.converter.moshi)
                implementation(libs.retrofit2.converter.scalars)
                implementation(libs.paho.mqtt.client)
                implementation(libs.apache.commons.text)
                implementation(libs.moshi.kotlin)
                implementation(libs.moshi.adapters)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core)
                implementation(libs.androidx.media3.exoplayer)
                implementation(libs.androidx.media3.ui)
                implementation(libs.androidx.navigation.compose)

                implementation(libs.android.colorx)
                implementation(libs.apollo3.runtime)
                implementation(libs.apollo3.adapters)

                implementation(libs.compose.markdown)
                implementation(libs.compose.permissions)

                implementation(libs.hive.mqtt.client)
                implementation(libs.sentry)

                implementation(libs.coil.compose)
                implementation(libs.coil.gif)

                implementation(libs.contacts.async)
                implementation(libs.contacts.core)
                implementation(libs.contacts.vcard)

                implementation(libs.datafaker)
                implementation(libs.emoji.picker)
                implementation(libs.giphy)

                implementation(libs.google.play.services.location)

                implementation(firebaseLibs.firebaseMessagingKtx)

                implementation(libs.square.okhttp3)
                implementation(libs.square.okhttp3.logging)

                implementation(libs.ok2curl)
//                implementation("com.google.android.material:material:1.9.0")
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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("String", "ENV", "\"production\"")
            buildConfigField("String", "HOST", "\"chat.botstacks.ai\"")
            buildConfigField("boolean", "SSL", "true")
        }
        getByName("debug") {
            buildConfigField("String", "ENV", "\"production\"")
            buildConfigField("String", "HOST", "\"chat.botstacks.ai\"")
            buildConfigField("boolean", "SSL", "true")
        }
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
            version = libraryVersion

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
fun gradleLocalProperties(projectRootDir : File) : Properties {
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