package xyz.catfootbeats.maiup.ui.theme

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController
import top.yukonga.miuix.kmp.utils.Platform
import top.yukonga.miuix.kmp.utils.platform
import xyz.catfootbeats.maiup.model.ThemeMode
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val viewModel: MaiupViewModel = koinViewModel()
    val settings by viewModel.settings.collectAsState()

    // 根据主题模式确定是否为深色主题
    val isDarkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    // 根据 isMonet 设置确定颜色方案模式
    val colorSchemeMode = when {
        settings.isMonet && isDarkTheme -> ColorSchemeMode.MonetDark
        settings.isMonet && !isDarkTheme -> ColorSchemeMode.MonetLight
        isDarkTheme -> ColorSchemeMode.Dark
        else -> ColorSchemeMode.Light
    }

    // 使用 remember 和 key 来确保当设置变化时重新创建 controller
    val controller = remember(isDarkTheme, settings.isMonet, settings.keyColor) {
        ThemeController(
            colorSchemeMode,
            keyColor = settings.keyColor,
            isDark = isDarkTheme
        )
    }

    if (platform() != Platform.MacOS) ComposeFoundationFlags.isNewContextMenuEnabled = true
    MiuixTheme(
        controller = controller,
    ) {
        content()
    }
}

