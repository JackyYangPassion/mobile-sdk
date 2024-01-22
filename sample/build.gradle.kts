plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    id("com.google.gms.google-services")
    id("com.github.triplet.play") version "3.8.4"
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":sdk"))
                implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
                implementation("com.google.firebase:firebase-analytics-ktx")
                implementation("com.giphy.sdk:ui:2.3.8")


                implementation("androidx.core:core-ktx:1.10.1")
                implementation("androidx.navigation:navigation-compose:2.7.1")

                implementation("androidx.core:core-ktx:1.10.1")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
                implementation("androidx.activity:activity-compose:1.7.2")
                implementation("androidx.core:core-ktx:1.10.1")
                implementation("androidx.work:work-runtime-ktx:2.8.1")
                implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
                implementation("androidx.core:core-splashscreen:1.0.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation("androidx.test.ext:junit:1.1.5")
                implementation("androidx.test.espresso:espresso-core:3.5.1")
                implementation("androidx.compose.ui:ui-test-junit4:1.5.0")
            }
        }
    }
}

android {
    namespace = "ai.botstacks.sample"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.compileSdk.get().toInt()
        versionCode = 6
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        resValue("string", "giphy", rootProject.ext["giphyIAC"] as String)
        resValue("string", "inappchat", rootProject.ext["inappchatApiKey"] as String)
    }

    signingConfigs {
        create("release") {
            val keyFile = file("../playstore")
            if (keyFile.exists()) {
                val storePass = project.properties["store.password"] as? String ?: ""
                val alias = project.properties["key.alias"] as? String ?: ""
                val keyPass = project.properties["key.password"] as? String ?: ""
                if (storePass.isNotEmpty() && alias.isNotEmpty() && keyPass.isNotEmpty()) {
                    storeFile = keyFile
                    storePassword = storePass
                    keyAlias = alias
                    keyPassword = keyPass
                }
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),("proguard-rules.pro"))
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            pickFirsts += listOf("META-INF/INDEX.LIST","META-INF/io.netty.versions.properties")
        }
    }
}

play {
    serviceAccountCredentials.set(file("../gpp.json"))
}