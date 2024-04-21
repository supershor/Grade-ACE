plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.om_tat_sat.grade_ace"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.om_tat_sat.grade_ace"
        minSdk = 24
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
}

dependencies {
    //noinspection UseTomlInstead
    implementation ("com.airbnb.android:lottie:6.2.0")
    //noinspection UseTomlInstead
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    //noinspection UseTomlInstead
    implementation ("com.intuit.ssp:ssp-android:1.1.0")
    //noinspection UseTomlInstead
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}