package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.ColorPalette
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.extra.WindowDialog
import top.yukonga.miuix.kmp.extra.WindowDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.AppConfig
import xyz.catfootbeats.maiup.model.ThemeMode
import xyz.catfootbeats.maiup.model.getName
import xyz.catfootbeats.maiup.utils.openUrl
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

@Composable
fun SettingsPage() {
    val maiup: MaiupViewModel = koinViewModel()
    val settings by maiup.settings.collectAsState()
    val isAuthorizing by maiup.isAuthorizing.collectAsState()
    val oauthError by maiup.oauthError.collectAsState()

    val showColorDialog = remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(settings.keyColor) }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // ---- OAuth ----
         Card {
             SuperArrow(
                 title = "落雪 OAuth 授权",
                 summary = when {
                     isAuthorizing -> "授权中..."
                     settings.isAuthorized -> "已授权"
                     else -> "未授权"
                 },
                 onClick = { maiup.authorizeOAuth() }
             )
             if (settings.isAuthorized) {
                 SuperArrow(
                     title = "清除授权",
                     summary = "移除访问令牌",
                     onClick = { maiup.clearOAuth() }
                 )
             }
         }

        oauthError?.let { error ->
            Text(
                text = error,
                color = MiuixTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // ---- 主题 ----
        Card {
            WindowDropdown(
                title = "主题",
                items = ThemeMode.getList().map { it.getName() },
                selectedIndex = settings.themeMode.ordinal,
                onSelectedIndexChange = { maiup.updateTheme(ThemeMode.fromInt(it)) }
            )
            SuperSwitch(
                title = "动态取色",
                checked = settings.isMonet,
                onCheckedChange = { maiup.updateMonet(it) }
            )
            SuperArrow(
                title = "选择颜色",
                summary = "#" + settings.keyColor.value.toHexString(HexFormat.UpperCase).take(8),
                enabled = settings.isMonet,
                onClick = { showColorDialog.value = true }
            )
        }

        // ---- 关于 ----
        Card {
            SuperArrow(
                title = "检查更新",
                summary = "Version: ${AppConfig.VERSION_NAME}",
                onClick = { /* TODO */ }
            )
            SuperArrow(
                title = "查看源代码",
                summary = "GitHub",
                onClick = { openUrl("https://github.com/Catfootbeats/maiup") }
            )
        }
    }

    // 授权中加载指示器
    if (isAuthorizing) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("等待浏览器授权...")
            }
        }
    }

    WindowDialog(
        title = "选择颜色",
        show = showColorDialog,
        onDismissRequest = { showColorDialog.value = false }
    ) {
        Column {
            ColorPalette(color = selectedColor, onColorChanged = { selectedColor = it })
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    text = "取消",
                    onClick = { showColorDialog.value = false }
                )
                TextButton(
                    modifier = Modifier.weight(1f),
                    text = "确定",
                    colors = ButtonDefaults.textButtonColorsPrimary(),
                    onClick = {
                        showColorDialog.value = false
                        maiup.updateKeyColor(selectedColor)
                    }
                )
            }
        }
    }
}
