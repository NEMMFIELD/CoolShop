// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlinSerializaiton) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.safeargs) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.8.1"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }

}

