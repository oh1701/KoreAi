plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.com.google.devtools.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.symbol.processing.api)
    implementation(project(":annotation"))
}