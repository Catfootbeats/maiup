package xyz.catfootbeats.maiup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import xyz.catfootbeats.maiup.di.appModule

fun main() {
    application {
    // 启动Koin
    startKoin {
        modules(appModule)
    }
    
    Window(
        onCloseRequest = {
            // 退出应用时关闭Koin
            org.koin.core.context.stopKoin()
            exitApplication()
        },
        title = "MaiUp!!!"
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            App()
        }
    }
}}