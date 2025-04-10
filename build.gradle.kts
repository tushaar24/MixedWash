plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.buildKonfig) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
//    kotlin("plugin.serialization") version libs.versions.kotlin
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false

}