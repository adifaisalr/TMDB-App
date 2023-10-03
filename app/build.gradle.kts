plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
}

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))

android {
    namespace = "com.adifaisalr.tmdbapplication"
    compileSdk = libs.versions.compilesdk.get().toInt()
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "com.adifaisalr.tmdbapplication"
        minSdk = libs.versions.minsdk.get().toInt()
        targetSdk = libs.versions.targetsdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_KEY_V3", apikeyProperties['API_KEY_V3'])

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
    dataBinding {
        enabled = true
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions{
        kotlinCompilerExtensionVersion = "1.4.8"
    }
}

dependencies {
    implementation(libs.retrofit.runtime)
    implementation(libs.retrofit.gson)
    implementation(libs.timber)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.bundles.lifecycle)

    implementation(libs.glide.runtime)
    implementation(libs.annotations)
    implementation libs.navigation.fragment
    implementation deps.navigation.ui.ktx
    implementation(libs.dagger.hilt.android)
    implementation deps.okhttp.mock_web_server
    implementation deps.okhttp.logging_interceptor

    implementation deps.fb_shimmer
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    // Jetpack Compose
    def composeBom = platform("androidx.compose:compose-bom:$versions.compose_bom")
    implementation composeBom
    androidTestImplementation composeBom
    implementation deps.compose.material3
    implementation deps.compose.ui_tooling_preview
    debugImplementation deps.compose.ui_tooling
    implementation deps.compose.activity
    implementation deps.compose.viewmodel
    implementation deps.compose.livedata

    debugImplementation deps.flipper.flipper
    debugImplementation deps.flipper.network
    debugImplementation deps.soloader

    releaseImplementation  deps.flipper.noop

    kapt(libs.lifecycle.compiler)
    kapt(libs.glide.compiler)
    kapt(libs.dagger.hilt.compiler)

    testImplementation project(path: ':app')
    testImplementation deps.junit
    testImplementation deps.okhttp.mock_web_server
    testImplementation deps.arch_core.testing
    testImplementation deps.mockito.core
    testImplementation deps.coroutines.android
    testImplementation deps.coroutines.test
    testImplementation deps.mockk

    androidTestImplementation deps.espresso.core
    androidTestImplementation deps.espresso.contrib
}