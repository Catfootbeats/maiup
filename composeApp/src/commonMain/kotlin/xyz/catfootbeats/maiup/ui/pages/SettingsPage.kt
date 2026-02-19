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
                    ThemeToggler(maiupViewModel)
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

@Composable
fun SettingsCard(
    title: String,
    tips: String = "",
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .width(500.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            //contentColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Column(
                content=content,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            if(tips!=""){
                Spacer(Modifier.height(8.dp))
                Text(
                    text = tips,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SettingItemRow(text: String, content: (@Composable RowScope.() -> Unit)){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = text,style = MaterialTheme.typography.bodyMedium)
        content()
    }
}

@Composable
fun SettingItemColumn(text: String, content: (@Composable ColumnScope.() -> Unit)){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
    ){
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium)
        content()
    }
}
@Composable
fun ThemeToggler(vm: MaiupViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val settings by vm.settingsState.collectAsState()

    Box {
        TextButton(
            onClick = { expanded = true },
        ) {
                Text(
                    text = when (settings.themeMode) {
                        ThemeMode.LIGHT -> "浅色模式"
                        ThemeMode.DARK -> "深色模式"
                        ThemeMode.SYSTEM -> "跟随系统"
                    }
                )
            /*
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "展开菜单",
                )*/
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp)
        ) {
            DropdownMenuItem(
                text = { Text("跟随系统") },
                onClick = {
                    vm.updateTheme(ThemeMode.SYSTEM)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.BrightnessAuto,
                        contentDescription = "跟随系统"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("浅色模式") },
                onClick = {
                    vm.updateTheme(ThemeMode.LIGHT)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LightMode,
                        contentDescription = "浅色模式"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("深色模式") },
                onClick = {
                    vm.updateTheme(ThemeMode.DARK)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = "深色模式"
                    )
                }
            )
        }
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
                )
            }
        }
    )
}
