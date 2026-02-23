package xyz.catfootbeats.maiup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import top.yukonga.miuix.kmp.basic.Surface
import xyz.catfootbeats.maiup.ui.pages.NavBar
import xyz.catfootbeats.maiup.ui.theme.AppTheme


@Composable
@Preview
fun App() {
    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavBar()
        }
    }
}