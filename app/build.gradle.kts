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
    //hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    //datastore
    implementation(libs.datastore.preferences)

    //navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.features)

    //leak canary
    debugImplementation(libs.leak.canary)

    //splash screen
    implementation(libs.splash.screen)

    //projects implementation
    implementation(project(":core:utils"))
    implementation(project(":core:coolshop-api"))
    implementation(project(":core:models"))
    implementation(project(":features:coolshop-main"))
    implementation(project(":features:coolshop-details"))
    implementation(project(":features:coolshop-cart"))
    implementation(project(":features:coolshop-user"))
    implementation(project(":features:coolshop-reviews"))
    implementation(project(":core:database"))

    //tests implementation
    testImplementation(libs.junit)
    testImplementation(project(":core:State"))
}
