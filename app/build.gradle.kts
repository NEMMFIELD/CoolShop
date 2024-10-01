plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.example.coolshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.coolshop"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":core:utils"))
    testImplementation(libs.junit)
    testImplementation(project(":core:State"))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.datastore.preferences)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.features)
    debugImplementation(libs.leak.canary)
    implementation(libs.splash.screen)
    implementation(project(":core:coolshop-api"))
    implementation(project(":core:data"))
    implementation(project(":features:coolshop-main"))
    implementation(project(":features:coolshop-details"))
    implementation(project(":features:coolshop-cart"))
    implementation(project(":features:coolshop-user"))
    implementation(project(":features:coolshop-reviews"))
    implementation(project(":core:database"))
}
