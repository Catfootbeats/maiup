package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import org.koin.compose.viewmodel.koinViewModel
import xyz.catfootbeats.maiup.AppConfig
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel
import xyz.catfootbeats.maiup.model.ThemeMode
import xyz.catfootbeats.maiup.ui.components.PasswordTextField
import xyz.catfootbeats.maiup.ui.components.SettingItemColumn
import xyz.catfootbeats.maiup.ui.components.SettingItemRow
import xyz.catfootbeats.maiup.ui.components.SettingsCard
import xyz.catfootbeats.maiup.ui.components.ThemeToggler
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.utils.openUrl

@Composable
fun SettingsPage(){
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val settings by maiupViewModel.settingsState.collectAsState()
    val focusManager = LocalFocusManager.current
    val playerDataViewModel: PlayerDataViewModel = koinViewModel()
    
    // 防抖处理
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by rememberSaveable { mutableStateOf<Job?>(null) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            SettingsCard("查分器","数据来自落雪查分器，水鱼仅同步成绩。"){
                SettingItemColumn("落雪 API 密钥"){
                    PasswordTextField(
                        value = settings.lxnsToken,
                        onValueChange = { 
                            maiupViewModel.updateLxnsAPI(it)
                            // 防抖:取消之前的任务,启动新的任务
                            debounceJob?.cancel()
                            debounceJob = coroutineScope.launch {
                                delay(3000)
                                // 检查token是否在3秒内没有变化
                                if (settings.lxnsToken == it) {
                                    playerDataViewModel.reload(it)
                                }
                            }
                        },
                        placeholder = "请输入API密钥"
                    )
                }
                SettingItemColumn("水鱼成绩导入 Token"){
                    PasswordTextField(
                        value = settings.waterfishToken,
                        onValueChange = { maiupViewModel.updateWaterfishToken(it) },
                        placeholder = "请输入Token"
                    )
                }
            }
        }
        item {
            SettingsCard("外观"){
                SettingItemRow("主题"){
                    ThemeToggler()
                }
            }
        }
        item {
            SettingsCard("关于 MaiUp"){
                SettingItemRow("Version: ${AppConfig.VERSION_NAME}"){
                    TextButton(
                        onClick = { /* TODO 检查更新 */ },
                    ){
                        Text("检查更新")
                    }
                }
                SettingItemRow("查看源代码"){
                    TextButton(
                        onClick = { openUrl("https://github.com/Catfootbeats/maiup") },
                    ){
                        Text("GitHub")
                    }
                }
            }
        }
    }
}
