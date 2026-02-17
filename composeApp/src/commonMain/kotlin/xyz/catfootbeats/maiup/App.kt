package xyz.catfootbeats.maiup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import xyz.catfootbeats.maiup.data.ThemeMode
import xyz.catfootbeats.maiup.ui.pages.NavBar
import xyz.catfootbeats.maiup.ui.theme.AppTheme
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel


@Composable
@Preview
fun App() {
    val vm: MaiupViewModel = koinViewModel()
    val settings by vm.settingsState.collectAsState()

    val darkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    AppTheme(
        darkTheme = darkTheme
    ) {
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