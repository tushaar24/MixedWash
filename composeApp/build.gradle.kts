import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.roomGradlePlugin)
    id("com.google.devtools.ksp")
}

room {
    schemaDirectory("$projectDir/schemas")
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
            implementation(libs.kotlinx.coroutines.android)

            // koin
            implementation(libs.koin.android)
//            implementation(libs.koin.androidx.compose)

            // ktor
            implementation(libs.ktor.client.okhttp)

            // firebase (notifications)
            implementation(project.dependencies.platform(libs.android.firebase.bom))
            implementation(libs.google.firebase.analytics)

            // firebase auth
            // implementation(libs.firebase.auth)
            // implementation(libs.play.services.auth)

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
            implementation(libs.kotlinx.coroutines.core)


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

            // room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // datastore
            implementation(libs.kotlinx.atomicfu)               // for kmp-notifier too
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.kotlinx.atomicfu)

            // kmpauth
            implementation(libs.kmpauth.google)     //Google One Tap Sign-In
            implementation(libs.kmpauth.firebase)   //Integrated Authentications with Firebase
            implementation(libs.kmpauth.uihelper)   //UiHelper SignIn buttons (AppleSignIn, GoogleSignInButton)


            // firebase firestore
            implementation(libs.gitlive.firebase.firestore)

            // lottie
            implementation (libs.compottie)

        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
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
    signingConfigs {
        create("sharedDebug") {
            storeFile = rootProject.file("debug_keystore.jks")
            storePassword = "emmawatson"
            keyAlias = "key0"
            keyPassword = "emmawatson"
        }
        create("release") {
            storeFile = rootProject.file("release_keystore.jks")
            storePassword = "emmastone"
            keyAlias = "key0"
            keyPassword = "emmastone"
        }

    }

    buildTypes {
        create("prod") {
            applicationIdSuffix = ".prod"
            isMinifyEnabled = true
            buildConfigField(type= "boolean", name = "BYPASS_LOCATION_CHECK", value = "false" )
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        release {
            applicationIdSuffix = ".release"
            isMinifyEnabled = false
            buildConfigField(type= "boolean", name = "BYPASS_LOCATION_CHECK", value = "false" )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            buildConfigField(type= "boolean", name = "BYPASS_LOCATION_CHECK", value = "false" )
            signingConfig = signingConfigs.getByName("sharedDebug")
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

    // room
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)

}

val apiKeys = Properties()
val testApiKeysFile = rootProject.file("test_api_keys.properties")
apiKeys.load(testApiKeysFile.inputStream())

val googleApiKey = apiKeys.getProperty("loki_test_google_api_key") ?: ""
val rzrpayTestKeyId = apiKeys.getProperty("rzrpay_test_key_id") ?: ""
val rzrpayTestKeySecret = apiKeys.getProperty("rzrpay_test_key_secret") ?: ""

buildkonfig {
    packageName = "com.mixedwash"
    objectName = "TestApiKeyConfig"

    defaultConfigs {
        buildConfigField(STRING, "googleApiKey", googleApiKey)
        buildConfigField(STRING, "rzrpayTestKeyId", rzrpayTestKeyId)
        buildConfigField(STRING, "rzrpayTestKeySecret", rzrpayTestKeySecret)
    }
}


