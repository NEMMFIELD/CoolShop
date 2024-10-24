plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.example.coolshop.reviews"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

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

    //recyclerView
    implementation(libs.recyclerview)

    //navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.features)
    implementation(libs.navigation.ui)

    //hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    
    //tests implementation
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.robolectric)
    testImplementation (libs.androidx.core.testing)

    //implementation projects
    implementation(project(":core:database"))
    implementation(project(":core:utils"))
    implementation(project(":core:models"))
    implementation(project(":core:State"))
}
