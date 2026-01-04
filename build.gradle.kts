// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.com.google.gms.google.services) apply false
    alias(libs.plugins.com.google.firebase.crashlytics) apply false
}


ext {
    set("testBuildType", project.properties["testBuildType"]?.toString() ?: "debug")
    set("projectPath", "devgyu.koreAi.")
}

with(tasks){
    register("DataModuleDebugAndroidTestTask"){
        doLast {
            exec { commandLine("./gradlew", ":data:connectedDebugAndroidTest", "-PtestBuildType=debug") }
        }
    }

    register("DataModuleReleaseAndroidTestTask"){
        doLast {
            exec { commandLine("./gradlew", ":data:connectedReleaseAndroidTest", "-PtestBuildType=release") }
        }
    }

    register("DataModuleAndroidTestTask"){
        dependsOn("DataModuleDebugAndroidTestTask", "DataModuleReleaseAndroidTestTask")
    }
}