plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerializaiton)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    //coroutines
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)

    //retrofit+moshi+interceptor
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization)
    api(libs.logging.interceptor)
    api(libs.moshi.kotlin)
    api(libs.moshi)
    api(libs.moshi.converter)
    implementation(libs.retrofit.adapter)
}
