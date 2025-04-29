plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "org.ralberth.areyouok"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.ralberth.areyouok"
        minSdk = 29           // This is the version of my Samsung Galaxy S9

        //noinspection ExpiredTargetSdkVersion
        targetSdk = 29        // TODO: must be higher to list on Google Play

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
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("org.jetbrains.kotlin:kotlin-stdlib") // for math

    // For Dependency Injection
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    // For Clock and other datetime (Kotlin-specific)
//    implementation("kotlinx.datetime:0.6.2")
//    implementation("kotlinx.datetime.format:0.6.2")
}


// Allow references to generated code
kapt {
    correctErrorTypes = true
}
