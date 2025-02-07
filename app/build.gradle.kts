plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlinAndroid)
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\ashis\\OneDrive\\Documents\\Android keys\\grade_ace.jks")
            storePassword = "ashish72240"
            keyPassword = "ashish72240"
            keyAlias = "key0"
        }
    }
    namespace = "com.om_tat_sat.grade_ace"
    compileSdk = 35
    bundle{
        language{
            enableSplit = false
        }
    }
    defaultConfig {
        applicationId = "com.om_tat_sat.grade_ace"
        minSdk = 24
        targetSdk = 35
        versionCode = 20000005
        versionName = "2.0.0.0.0.0.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //noinspection UseTomlInstead
        implementation ("com.github.shobhitpuri:custom-google-signin-button:2.0.0")

    //noinspection UseTomlInstead
    implementation ("com.google.android.gms:play-services-auth:19.2.0")



    //noinspection UseTomlInstead
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //noinspection UseTomlInstead
    implementation ("com.github.barteksc:AndroidPdfViewerV1:1.6.0")

    //noinspection UseTomlInstead
    implementation ("com.jjoe64:graphview:4.2.2")
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
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-auth")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}