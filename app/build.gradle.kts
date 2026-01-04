import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.api.variant.BuildConfigField
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.gms.google.services)
    alias(libs.plugins.com.google.firebase.crashlytics)
}

android {
    namespace = rootProject.ext["projectPath"] as String + "app"
    compileSdk = 34
    testBuildType = rootProject.ext["testBuildType"] as String

    defaultConfig {
        applicationId = namespace
        minSdk = 26
        targetSdk = 34
        versionCode = 6
        versionName = "1.0.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        fun ApkSigningConfig.getKeyStoreSettings() {
            val storeFileValue = if (System.getenv("BITRISE_KEYSTORE_PATH") != null) {
                file(System.getenv("BITRISE_KEYSTORE_PATH"))
            } else {
                file(getProperty("KEYSTORE_PATH"))
            }

            keyAlias = System.getenv("BITRISEIO_ANDROID_KEYSTORE_ALIAS") ?: getProperty("KEY_ALIAS")
            keyPassword =
                System.getenv("BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD") ?: getProperty("KEY_PASSWORD")
            storeFile = storeFileValue
            storePassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PASSWORD") ?: getProperty("STORE_PASSWORD")
        }

        create("release") { getKeyStoreSettings() }
        getByName("debug") { getKeyStoreSettings() }
    }

    buildTypes {
        release {
            manifestPlaceholders["admob_main_ads_id"] = getProperty("RELESE_ADMOB_MAIN_ADS_ID")
            signingConfig = signingConfigs.getByName("release")
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            manifestPlaceholders["admob_main_ads_id"] = getProperty("DEBUG_ADMOB_MAIN_ADS_ID")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core"))

    // Firebase
    val firebaseBom = platform(libs.firebase.bom)
    implementation(firebaseBom)
    implementation(libs.bundles.firebase)

    // Timber
    implementation(libs.timber)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Ktor
    implementation(libs.bundles.ktor)

    // Google 광고
    implementation(libs.play.services.ads)
}

fun getProperty(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
}