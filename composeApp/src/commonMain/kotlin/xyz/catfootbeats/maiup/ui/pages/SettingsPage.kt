package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.extra.WindowDialog
import top.yukonga.miuix.kmp.extra.WindowDropdown
import top.yukonga.miuix.kmp.basic.ColorPalette
import xyz.catfootbeats.maiup.AppConfig
import xyz.catfootbeats.maiup.model.ThemeMode
import xyz.catfootbeats.maiup.model.getName
import xyz.catfootbeats.maiup.ui.components.*
import xyz.catfootbeats.maiup.utils.openUrl
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun SettingsPage() {
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val settings by maiupViewModel.settings.collectAsState()
    val playerDataViewModel: PlayerDataViewModel = koinViewModel()

    // for ui
    val showColorDialog = remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(settings.keyColor) }
    val showLxnsDialog = remember { mutableStateOf(false) }
    val showWaterfishDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card {
            SuperArrow(
                title = "落雪设定",
                summary = "数据来源",
                onClick = { showLxnsDialog.value = true }
            )
            SuperArrow(
                title = "落雪 OAuth 授权",
                summary = "未授权",
                onClick = { /*启动Http服务，监听回调，跳转到外部浏览器授权地址*/ }
            )
            SuperArrow(
                title = "水鱼设定",
                summary = "暂未支持 / 仅同步数据",
                enabled = false,
                onClick = { /* TODO 检查更新 */ }
            )
        }

        Card {
            WindowDropdown(
                title = "主题",
                items = ThemeMode.getList().map { it.getName() },
                selectedIndex = settings.themeMode.ordinal,
                onSelectedIndexChange = {
                    maiupViewModel.updateTheme(ThemeMode.fromInt(it))
                }
            )
            SuperSwitch(
                title = "动态取色",
                checked = settings.isMonet,
                onCheckedChange = { maiupViewModel.updateMonet(it) }
            )
            SuperArrow(
                title = "选择颜色",
                summary =
                    "#"+settings.keyColor.value.toHexString(HexFormat.UpperCase).take(8),
                enabled = settings.isMonet,
                onClick = { showColorDialog.value = true }
            )
        }

        Card {
            SuperArrow(
                title = "检查更新",
                summary = "Version: ${AppConfig.VERSION_NAME}",
                onClick = { /* TODO 检查更新 */ }
            )
            SuperArrow(
                title = "查看源代码",
                summary = "GitHub",
                onClick = { openUrl("https://github.com/Catfootbeats/maiup") }
            )
        }
    }

    WindowDialog(
        title = "选择颜色",
        show = showColorDialog,
        onDismissRequest = { showColorDialog.value = false } // 关闭对话框
    ) {
        Column {
            ColorPalette(
                color = selectedColor,
                onColorChanged = { selectedColor = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    text = "取消",
                    onClick = { showColorDialog.value = false } // 关闭对话框
                )
                TextButton(
                    modifier = Modifier.weight(1f),
                    text = "确定",
                    colors = ButtonDefaults.textButtonColorsPrimary(), // 使用主题颜色
                    onClick = {
                        showColorDialog.value = false // 关闭对话框
                        maiupViewModel.updateKeyColor(selectedColor)
                    })
            }
        }
    }

    WindowDialog(
        title = "落雪设定",
        summary = null,
        show = showLxnsDialog,
        onDismissRequest = {
            if(!settings.lxnsToken.isEmpty()) {
                showLxnsDialog.value = false
                playerDataViewModel.reload(settings.lxnsToken)
            }
        } // 关闭对话框
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PasswordTextField(
                value = settings.lxnsToken,
                lable = "落雪 API 密钥",
                onValueChange = {
                    maiupViewModel.updateLxnsAPI(it)
                },
            )
            TextButton(
                text = "确定",
                onClick = {
                    if(!settings.lxnsToken.isEmpty()) {
                        showLxnsDialog.value = false
                        playerDataViewModel.reload(settings.lxnsToken)
                    }
                }, // 关闭对话框
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    WindowDialog(
        title = "水鱼设定",
        summary = null,
        show = showWaterfishDialog,
        onDismissRequest = { showWaterfishDialog.value = false } // 关闭对话框
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PasswordTextField(
                value = settings.waterfishToken,
                lable = "水鱼 Token",
                onValueChange = {
                    maiupViewModel.updateWaterfishToken(it)
                },
            )
            TextButton(
                text = "确定",
                onClick = { showLxnsDialog.value = false }, // 关闭对话框
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
