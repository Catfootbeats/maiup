import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.*

// 从 gradle.properties 或外部文件读取版本号
val appVersionName = project.properties["versionName"] as? String ?: "0"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    // alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            // Ktor Android 引擎
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            api(libs.androidx.lifecycle.viewmodel)
            // material3 adaptive navigation suite
            implementation(libs.material3.adaptive.navigation.suite)
            // material icons
            implementation(libs.material)
            implementation(libs.material.icons.extended)
            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // datastore
            implementation(libs.datastore.preferences)
            // Ktor 客户端
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            // Kotlinx 序列化
            implementation(libs.kotlinx.serialization.json)
            // 在线图片
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor2)
            // 图表
            implementation(libs.compose.charts)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            // Ktor JVM 引擎
            implementation(libs.ktor.client.cio)
            implementation(libs.slf4j.simple)
        }
        iosMain.dependencies {
            // Ktor iOS 引擎
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "xyz.catfootbeats.maiup"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "xyz.catfootbeats.maiup"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = appVersionName
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false         // 应用优化
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "xyz.catfootbeats.maiup.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = "xyz.catfootbeats.maiup"
            packageVersion = appVersionName
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "xyz.catfootbeats.maiup.resources"
    generateResClass = auto
}

buildkonfig {
    packageName = "xyz.catfootbeats.maiup"
    objectName = "AppConfig"
    defaultConfigs {
        // 定义字段：类型、字段名、值
        buildConfigField(STRING, "VERSION_NAME", appVersionName) // 注意字符串需要转义引号
    }
}
