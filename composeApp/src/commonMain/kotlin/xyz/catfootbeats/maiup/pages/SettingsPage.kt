package xyz.catfootbeats.maiup.pages

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import xyz.catfootbeats.maiup.utils.getAppVersion

@Composable
fun SettingsPage(){
    var lxnsApiKey by remember { mutableStateOf("") }
    var waterfishToken by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val appVersion = getAppVersion()
    
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
            SettingsCard("外观"){
                SettingItemRow("主题"){
                    ThemeToggle()
                }
            }
        }
        item {
            SettingsCard("查分器","数据来自落雪查分器，水鱼仅同步成绩。"){
                SettingItemColumn("落雪 API 密钥"){
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = lxnsApiKey,
                        onValueChange = { lxnsApiKey = it },
                        placeholder = { Text("请输入API密钥") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                    )
                }
                SettingItemColumn("水鱼成绩导入 Token"){
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = waterfishToken,
                        onValueChange = { waterfishToken = it },
                        placeholder = { Text("请输入Token") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                    )
                }
            }
        }
        item {
            SettingsCard("关于 MaiUp"){
                SettingItemRow("Version: $appVersion"){
                    TextButton(
                        onClick = { /* TODO 检查更新 */ },
                    ){
                        Text("检查更新")
                    }
                }
                SettingItemRow("查看源代码"){
                    TextButton(
                        onClick = { /* TODO 跳转源码 */ },
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@Composable
fun ThemeToggle() {
    var expanded by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf(ThemeMode.SYSTEM) }

    Box {
        TextButton(
            onClick = { expanded = true },
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
                Text(
                    text = when (selectedTheme) {
                        ThemeMode.LIGHT -> "浅色模式"
                        ThemeMode.DARK -> "深色模式"
                        ThemeMode.SYSTEM -> "跟随系统"
                    }
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "展开菜单",
                    modifier = Modifier.padding(start = 4.dp)
                )
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp)
        ) {
            DropdownMenuItem(
                text = { Text("跟随系统") },
                onClick = {
                    selectedTheme = ThemeMode.SYSTEM
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
                    selectedTheme = ThemeMode.LIGHT
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
                    selectedTheme = ThemeMode.DARK
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
