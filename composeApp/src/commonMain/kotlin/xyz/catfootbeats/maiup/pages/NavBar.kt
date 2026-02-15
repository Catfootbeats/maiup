package xyz.catfootbeats.maiup.pages

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
import xyz.catfootbeats.maiup.resources.Res
import xyz.catfootbeats.maiup.resources.chu
import xyz.catfootbeats.maiup.resources.mai

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
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var modeStr by remember { mutableStateOf("中二节奏") }// TODO实现模式更改

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
                    IconButton(onClick = { /* TODO: 实现同步功能 */ }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "同步"
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
                                    text = modeStr,
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
                                    modeStr = "中二节奏"
                                /* TODO: 切换到中二节奏模式 */ }
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
                                    modeStr = "舞萌DX"
                                /* TODO: 切换到舞萌DX模式 */ }
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