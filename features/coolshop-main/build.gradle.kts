plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.example.coolshop.main"
    compileSdk = 34

    packaging {
       exclude("META-INF/LICENSE.md")
    }

    defaultConfig {
        minSdk = 28
        lint.targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    //core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    api(libs.fragment)

    //recyclerView
    implementation(libs.recyclerview)

    //coil
    implementation(libs.coil)

    //dagger
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    //cardView
    implementation(libs.cardview)

    //navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.features)
    implementation(libs.navigation.ui)

    //splashActivity
    implementation(libs.splash.screen)

    //tests implementation
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.coroutines.test)
    testImplementation (libs.androidx.core.testing)

    //implementation projects
    implementation(project(":core:coolshop-api"))
    implementation(project(":core:models"))
    implementation(project(":core:database"))
    implementation(project(":core:State"))
    implementation(project(":features:coolshop-details"))
    implementation(project(":core:utils"))
}
