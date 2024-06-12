plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.tfg_carlosramos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tfg_carlosramos"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Asegúrate de usar solo una versión de Material Design
    implementation("com.google.android.material:material:1.12.0")

    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    // Retrofit para realizar solicitudes HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Convertidor Gson para Retrofit, para la serialización automática de objetos
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Opcional: Logging Interceptor para ver las solicitudes y respuestas en el log
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation(libs.core.ktx)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

