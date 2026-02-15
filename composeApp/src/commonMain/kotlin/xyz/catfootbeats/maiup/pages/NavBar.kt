package xyz.catfootbeats.maiup.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataExploration
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    // 主页 直接显示用户的成绩 头像 收藏品 背景框 点击即可跳转更新成绩 可以改变中二模式和舞萌模式
    HOME("主页", Icons.Default.Home,"账号详情"),
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
        Column {
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
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                                Text(
                                    text = "中二节奏",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "模式"
                                )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            offset = DpOffset(60.dp, 0.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ){
                                        Image(
                                            painter = painterResource(Res.drawable.chu),
                                            contentDescription = "中二节奏",
                                            modifier = Modifier.height(20.dp).padding(end = 4.dp)
                                        )
                                        Text("中二节奏")
                                    }
                                       },
                                onClick = { /* TODO: 切换到中二节奏模式 */ }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Image(
                                            painter = painterResource(Res.drawable.mai),
                                            contentDescription = "舞萌DX",
                                            modifier = Modifier.height(20.dp).padding(end = 4.dp)
                                        )
                                        Text("舞萌DX")
                                    }
                                       },
                                onClick = { /* TODO: 切换到舞萌DX模式 */ }
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