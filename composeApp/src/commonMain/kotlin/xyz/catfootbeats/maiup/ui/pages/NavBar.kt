package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.*
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.extra.LocalWindowBottomSheetState
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.WindowBottomSheet
import top.yukonga.miuix.kmp.extra.WindowListPopup
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.*
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import xyz.catfootbeats.maiup.model.Game
import xyz.catfootbeats.maiup.model.getName
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val desc: String,
) {
    // 首页 直接显示用户的成绩 头像 收藏品 背景框 点击即可跳转更新成绩 可以改变中二模式和舞萌模式
    HOME("首页", MiuixIcons.Contacts, "账号详情"),

    // 搜索 可以查找游玩的歌曲
    SEARCH("搜索", MiuixIcons.Music, "乐曲搜索"),

    // 成绩 可以查看 b50 与 历史成绩
    RANK("成绩", MiuixIcons.TopDownloads, "成绩"),

    // 设置 用于设置查分器 同步成绩设定
    SETTINGS("设置", MiuixIcons.Settings, "设置"),
}

@Composable
fun NavBar() {
    val maiupViewModel: MaiupViewModel = koinViewModel()

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    // haze 背景模糊
    val hazeState = remember { HazeState() }
    val hazeStyle = HazeStyle(
        backgroundColor = MiuixTheme.colorScheme.surface,
        tint = HazeTint(MiuixTheme.colorScheme.surface.copy(0.1f))
    )
    // 顶部模式栏
    val showPopup = remember { mutableStateOf(false) }
    val popupItems = listOf(Game.MAI, Game.CHU)
    // 同步对话框
    val showSyncSheet = remember { mutableStateOf(false) }

    val settings by maiupViewModel.settingsState.collectAsState()
    var upMai by remember { mutableStateOf(true) }
    var upChu by remember { mutableStateOf(false) }
    var upLxns by remember { mutableStateOf(settings.lxnsToken.isNotEmpty()) }
    var upWaterfish by remember { mutableStateOf(settings.waterfishToken.isNotEmpty()) }



    val topAppBarScrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())

    // 等待设置加载完成并延迟一秒
    LaunchedEffect(settings) {
        // 根据token状态更新同步选项
        upLxns = settings.lxnsToken.isNotEmpty()
        upWaterfish = settings.waterfishToken.isNotEmpty()
    }

    // 同步对话框
    WindowBottomSheet(
        show = showSyncSheet,
        title = "同步成绩",
        onDismissRequest = { showSyncSheet.value = false }
    ) {
        val dismiss = LocalWindowBottomSheetState.current
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Card {
                SuperCheckbox(
                    title = "舞萌DX",
                    checked = upMai,
                    onCheckedChange = { upMai = it }
                )
                SuperCheckbox(
                    title = "中二节奏",
                    checked = upChu,
                    onCheckedChange = { upChu = it }
                )
                HorizontalDivider()
                SuperCheckbox(
                    title = "落雪",
                    checked = upLxns,
                    enabled = settings.lxnsToken.isNotEmpty(),
                    onCheckedChange = { upLxns = it }
                )
                SuperCheckbox(
                    title = "水鱼",
                    checked = upWaterfish,
                    enabled = settings.waterfishToken.isNotEmpty(),
                    onCheckedChange = { upWaterfish = it }
                )
            }
            Spacer(Modifier.height(16.dp))
            TextButton(
                text = "同步",
                modifier = Modifier.fillMaxWidth(),
                onClick = { dismiss?.invoke() /*TODO：同步成绩*/ }
            )
        }

    }
    Row {
        if (LocalWindowInfo.current.containerSize.width.dp >= 1100.dp) {
            NavigationRail(
                color = Color.Transparent,
                modifier = Modifier
                    .hazeEffect(hazeState) {
                        style = hazeStyle
                        blurRadius = 25.dp
                        noiseFactor = 0f
                    }
            ) {
                AppDestinations.entries.forEach {
                    NavigationRailItem(
                        selected = currentDestination == it,
                        onClick = { currentDestination = it },
                        icon = it.icon,
                        label = it.label,
                    )
                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    color = Color.Transparent,
                    modifier = Modifier
                        .hazeEffect(hazeState) {
                            style = hazeStyle
                            blurRadius = 25.dp
                            noiseFactor = 0f
                        },
                    title = currentDestination.desc,
                    scrollBehavior = topAppBarScrollBehavior,
                    actions = {
                        // 同步按钮
                        IconButton(onClick = { showSyncSheet.value = true }) {
                            Icon(
                                imageVector = MiuixIcons.Update,
                                contentDescription = null
                            )
                        }
                        // 模式按钮
                        IconButton(onClick = { showPopup.value = true }) {
                            Icon(
                                imageVector = MiuixIcons.Tune,
                                contentDescription = null
                            )
                        }
                        WindowListPopup(
                            show = showPopup,
                            alignment = PopupPositionProvider.Align.Start,
                            onDismissRequest = { showPopup.value = false } // 关闭弹窗菜单
                        ) {
                            ListPopupColumn {
                                popupItems.forEachIndexed { index, game ->
                                    DropdownImpl(
                                        text = game.getName(),
                                        optionSize = popupItems.size,
                                        isSelected = settings.game == game,
                                        onSelectedIndexChange = {
                                            maiupViewModel.updateAppMode(game)
                                            showPopup.value = false // 关闭弹窗菜单
                                        },
                                        index = index
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                )
            },
            bottomBar = {
                if (LocalWindowInfo.current.containerSize.width.dp < 1100.dp) {
                    NavigationBar(
                        color = Color.Transparent,
                        modifier = Modifier
                            .hazeEffect(hazeState) {
                                style = hazeStyle
                                blurRadius = 25.dp
                                noiseFactor = 0f
                            }
                    ) {
                        AppDestinations.entries.forEach {
                            NavigationBarItem(
                                selected = currentDestination == it,
                                onClick = { currentDestination = it },
                                icon = it.icon,
                                label = it.label,
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            // 内容区域需要考虑 padding
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(state = hazeState)
                    // 如需添加越界回弹效果，则应在绑定滚动行为之前添加
                    .overScrollVertical()
                    // 绑定 TopAppBar 滚动事件
                    .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
            ) {
                item {
                    when (currentDestination) {
                        AppDestinations.HOME -> HomePage()
                        AppDestinations.SEARCH -> SearchPage()
                        AppDestinations.RANK -> RankPage()
                        AppDestinations.SETTINGS -> SettingsPage()
                    }
                    Spacer(Modifier.height(72.dp))
                }
            }
        }
    }
}