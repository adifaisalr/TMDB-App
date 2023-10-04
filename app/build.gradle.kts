import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))

fun <T> String.asPropertiesFile(block: Properties.() -> T): T? {
    file(this).takeIf { it.canRead() }?.inputStream()?.use {
        val properties = Properties().apply {
            load(it)
        }
        return properties.block()
    }
    return null
}

android {
    namespace = "com.adifaisalr.tmdbapplication"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "com.adifaisalr.tmdbapplication"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_KEY_V3", apikeyProperties.getProperty("API_KEY_V3"))

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
        jvmTarget = "11"
    }
    dataBinding {
        isEnabled = true
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
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.dagger.hilt.android)
    implementation(libs.okhttp.mockwebserver)
    implementation(libs.okhttp.logginginterceptor)

    implementation(libs.fb.shimmer)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.bom)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.activity)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.runtime.livedata)

    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network)
    debugImplementation(libs.soloader)
    releaseImplementation(libs.flipper.noop)

    ksp(libs.lifecycle.compiler)
    ksp(libs.glide.compiler)
    ksp(libs.dagger.hilt.compiler)

    testImplementation(project(":app"))
    testImplementation(deps.junit)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(deps.arch_core.testing)
    testImplementation(deps.mockito.core)
    testImplementation(deps.coroutines.android)
    testImplementation(deps.coroutines.test)
    testImplementation(deps.mockk)

    androidTestImplementation(deps.espresso.core)
    androidTestImplementation(deps.espresso.contrib)
}