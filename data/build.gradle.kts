import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = rootProject.ext["projectPath"] as String + "data"
    compileSdk = 34
    testBuildType = rootProject.ext["testBuildType"] as String

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "${namespace}.HiltTestRunner"
        buildConfigField("String", "FAL_API_KEY", getProperty("FAL_API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 구글 번역
    implementation(libs.google.translator)

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    kspTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android)
    kspAndroidTest(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android.testing)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Datastore
    implementation(libs.datastore.preferences)

    // Firebase
    val firebaseBom = platform(libs.firebase.bom)
    implementation(firebaseBom)
    implementation(libs.bundles.firebase)

    // Timber
    implementation(libs.timber)

    // Ktor
    implementation(libs.bundles.ktor)

    // coroutine Test
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test)
}

fun getProperty(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
}