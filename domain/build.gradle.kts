plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    // 안드로이드 의존성이 없는, 순수한 코틀린 자바 라이브러리들만 추가 가능
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)

    api(libs.kotlinx.collections.immutable)
}