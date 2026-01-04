plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = rootProject.ext["projectPath"] as String + "core"
    compileSdk = 34
    testBuildType = rootProject.ext["testBuildType"] as String

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)

    // Timber
    implementation(libs.timber)

    api(libs.kotlinx.collections.immutable)
}