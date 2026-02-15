package xyz.catfootbeats.maiup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "MaiUp!!!"
            ) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        App()
                    }

            }
}