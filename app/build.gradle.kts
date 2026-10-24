plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kampplus.hava"
    // compileSdk: derlemede erisilebilen Android API seviyesi.
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kampplus.hava"
        // minSdk: uygulamanin calisacagi en dusuk Android surumu.
        minSdk = 26
        // targetSdk: davranislarin hangi surume gore ayarlanacagi.
        targetSdk = 36
        // versionCode sayisal surum kodu, versionName kullaniciya gosterilen surum.
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)

    // @Preview'lari Android Studio'da gosterebilmek icin yalnizca debug derlemesinde.
    debugImplementation(libs.androidx.compose.ui.tooling)
}
