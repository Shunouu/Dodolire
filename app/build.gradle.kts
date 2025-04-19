plugins {
    id("com.android.application")
    // On n’utilise plus Kotlin dans le code, donc on retire les plugins org.jetbrains.kotlin.*
}

android {
    namespace = "com.example.dodolire"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dodolire"
        minSdk = 24
        targetSdk = 35
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

// On force la bonne version de kotlin-stdlib pour éviter les doublons
configurations.all {
    resolutionStrategy {
        force(
            "org.jetbrains.kotlin:kotlin-stdlib:1.8.10",
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.10",
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10"
        )
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    // HTTP client + JSON parsing
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.google.code.gson:gson:2.8.8")

    // Image loading (Glide)
    implementation("com.github.bumptech.glide:glide:4.12.0")
    // On passe à annotationProcessor pour Glide sans KAPT
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
