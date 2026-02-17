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
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.serialization)
    id("com.codingfeline.buildkonfig")
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
            implementation("io.ktor:ktor-client-okhttp:2.3.7")
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
            implementation("org.jetbrains.compose.material3:material3-adaptive-navigation-suite:1.9.0")
            implementation("org.jetbrains.compose.material:material:1.10.0")          // 基础 Material 组件 + 核心图标
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            implementation("io.insert-koin:koin-core:4.1.0")
            implementation("io.insert-koin:koin-compose:4.1.0")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.0")
            implementation("androidx.datastore:datastore-preferences:1.2.0")
            // Ktor 客户端
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            // Kotlinx 序列化
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            // 在线图片
            implementation("io.coil-kt.coil3:coil-compose:3.3.0")
            implementation("io.coil-kt.coil3:coil-network-ktor2:3.3.0")
            // 图表
            implementation ("io.github.ehsannarmani:compose-charts:0.2.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            // Ktor JVM 引擎
            implementation("io.ktor:ktor-client-cio:2.3.7")
            implementation("org.slf4j:slf4j-simple:2.0.9")
        }
        iosMain.dependencies {
            // Ktor iOS 引擎
            implementation("io.ktor:ktor-client-darwin:2.3.7")
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
            isMinifyEnabled = false
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
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
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
