package xyz.catfootbeats.maiup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.dsl.KoinAppDeclaration
import xyz.catfootbeats.maiup.pages.NavBar
import xyz.catfootbeats.maiup.theme.AppTheme


@Composable
@Preview
fun App() {
    AppTheme {
        Surface (
            color = MaterialTheme.colorScheme.surface, // 设置为主题的 surface 颜色
            contentColor = MaterialTheme.colorScheme.onSurface, // 自动匹配对比色
            modifier = Modifier
                //.safeContentPadding()
                .fillMaxSize()
        ) {
            NavBar()
        }
    }
}