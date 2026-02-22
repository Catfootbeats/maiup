plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    // android
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false  // 抛弃
    // alias(libs.plugins.androidMultiplatformLibrary) apply false // 新的
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false

    // alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.buildkonfig) apply false
}