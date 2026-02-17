package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import xyz.catfootbeats.maiup.data.AppMode
import xyz.catfootbeats.maiup.resources.Res
import xyz.catfootbeats.maiup.resources.chu
import xyz.catfootbeats.maiup.resources.mai
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val desc: String,
    ) {
    // 首页 直接显示用户的成绩 头像 收藏品 背景框 点击即可跳转更新成绩 可以改变中二模式和舞萌模式
    HOME("首页", Icons.Default.Home,"账号详情"),
    // 搜索 可以查找游玩的歌曲
    SEARCH("搜索", Icons.Default.MusicNote,"乐曲搜索"),
    // 成绩 可以查看 b50 与 历史成绩
    RANK("成绩", Icons.Default.DataExploration,"成绩"),
    // 设置 用于设置查分器 同步成绩设定
    SETTINGS("设置", Icons.Default.Settings,"设置"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(){
    val maiupViewModel: MaiupViewModel = koinViewModel()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val settings by maiupViewModel.settingsState.collectAsState()
    var syncMai by remember { mutableStateOf(true) }
    var syncChu by remember { mutableStateOf(true) }
    var syncLxns by remember { mutableStateOf(true) }
    var syncWaterfish by remember { mutableStateOf(true) }

    NavigationSuiteScaffold(
        modifier = Modifier,
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    modifier = Modifier.padding(8.dp),
                    icon = {
                        Column {
                            Icon(
                                it.icon,
                                contentDescription = it.label
                            )
                        }
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Column{
            TopAppBar(
                title = {
                    Text(
                        text = currentDestination.desc,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    // 同步按钮
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "同步"
                        )
                    }
                    // 同步对话框
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("同步成绩") },
                            text = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        "来源",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    // 第一排：舞萌DX, 中二节奏
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        FilterChip(
                                            selected = syncMai,
                                            onClick = { syncMai = !syncMai },
                                            label = { Text("舞萌DX") },
                                            leadingIcon = if (syncMai) {
                                                { Icon(Icons.Default.Check, contentDescription = null) }
                                            } else null,
                                            modifier = Modifier.weight(1f),
                                        )
                                        FilterChip(
                                            selected = syncChu,
                                            onClick = { syncChu = !syncChu },
                                            label = { Text("中二节奏") },
                                            leadingIcon = if (syncChu) {
                                                { Icon(Icons.Default.Check, contentDescription = null) }
                                            } else null,
                                            modifier = Modifier.weight(1f),
                                        )
                                    }
                                    Text(
                                        "查分器",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    // 第二排：落雪, 水鱼
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        FilterChip(
                                            selected = syncLxns,
                                            onClick = { syncLxns = !syncLxns },
                                            label = { Text("落雪") },
                                            leadingIcon = if (syncLxns) {
                                                { Icon(Icons.Default.Check, contentDescription = null) }
                                            } else null,
                                            modifier = Modifier.weight(1f),
                                        )
                                        FilterChip(
                                            selected = syncWaterfish,
                                            onClick = { syncWaterfish = !syncWaterfish },
                                            label = { Text("水鱼") },
                                            leadingIcon = if (syncWaterfish) {
                                                { Icon(Icons.Default.Check, contentDescription = null) }
                                            } else null,
                                            modifier = Modifier.weight(1f),
                                        )
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        // TODO: 实现同步功能
                                        showDialog = false
                                    }
                                ) {
                                    Text("同步")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDialog = false }
                                ) {
                                    Text("取消")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // 模式按钮
                        Button(
                            onClick = { expanded = !expanded },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(end = 10.dp) //为了让尾部和卡片对齐
                        ) {
                                Text(
                                    text = when (settings.appMode) {
                                        AppMode.CHU -> "中二节奏"
                                        AppMode.MAI -> "舞萌DX"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.width(60.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "模式"
                                )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            offset = DpOffset(50.dp, 0.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(Res.drawable.chu),
                                        contentDescription = "中二节奏",
                                        modifier = Modifier.height(24.dp)
                                    )
                                },
                                text = { Text("中二节奏") },
                                onClick = {
                                    expanded = false
                                    maiupViewModel.updateAppMode(AppMode.CHU)
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(Res.drawable.mai),
                                        contentDescription = "舞萌DX",
                                        modifier = Modifier.height(24.dp)
                                    )
                                },
                                text = { Text("舞萌DX") },
                                onClick = {
                                    expanded = false
                                    maiupViewModel.updateAppMode(AppMode.MAI)
                                }
                            )
                        }
                }
            )

            when (currentDestination){
                AppDestinations.HOME -> HomePage()
                AppDestinations.SEARCH -> SearchPage()
                AppDestinations.RANK -> RankPage()
                AppDestinations.SETTINGS -> SettingsPage()
            }
        }
    }
}