import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.buildKonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            // ktor
            implementation(libs.ktor.client.okhttp)

            // firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.google.firebase.analytics)

            // location
            api(libs.play.services.location)
            api(libs.play.services.coroutines)

            // startup
            implementation(libs.androidx.startup)



        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            api(libs.kotlinx.serialization.json)


            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.logging)


            // koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // datetime
            implementation(libs.kotlinx.datetime)

            // navigation
            implementation(libs.navigation.compose)

        }

        commonTest.dependencies {
            implementation(kotlin("test"))

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.kotlinx.atomicfu)
            api(libs.kmp.notifier)  // documentation says to use export...
        }

    }
}

android {
    namespace = "com.mixedwash"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mixedwash"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    debugImplementation(libs.androidx.ui.tooling)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("test_api_keys.properties")
localProperties.load(localPropertiesFile.inputStream())

val googleApiKey = localProperties.getProperty("loki_test_google_api_key") ?: ""
val rzrpayTestKeyId = localProperties.getProperty("rzrpay_test_key_id") ?: ""
val rzrpayTestKeySecret = localProperties.getProperty("rzrpay_test_key_secret") ?: ""

buildkonfig {
    packageName = "com.mixedwash"
    objectName = "TestApiKeyConfig"
    // exposeObjectWithName = "YourAwesomePublicConfig"

    defaultConfigs {
        buildConfigField(STRING, "googleApiKey", googleApiKey)
        buildConfigField(STRING, "rzrpayTestKeyId", rzrpayTestKeyId)
        buildConfigField(STRING, "rzrpayTestKeySecret", rzrpayTestKeySecret)
    }
}


