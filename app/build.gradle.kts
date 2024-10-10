plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myweatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myweatherapp"
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
    implementation(libs.appcompat)
    implementation(libs.material)
//    implementation(libs.mainActivity)
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.simpleHttpRequest) // Use the alias from libs.versions.toml
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

//    implementation("com.github.androdocs:Simple-HTTP-Request:v1.0")
//    implementation('com.github.bumptech.glide:glide:4.15.1')
//    annotationProcessor('com.github.bumptech.glide:compiler:4.15.1')
}