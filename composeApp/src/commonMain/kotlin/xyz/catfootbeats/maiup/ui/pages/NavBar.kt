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
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.extra.LocalWindowBottomSheetState
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.WindowBottomSheet
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.*
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val desc: String,
) {
    HOME("首页", MiuixIcons.Contacts, "账号详情"),
    SEARCH("搜索", MiuixIcons.Music, "搜索"),
    RANK("成绩", MiuixIcons.TopDownloads, "成绩"),
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
    // 同步对话框
    val showSyncSheet = remember { mutableStateOf(false) }

    val settings by maiupViewModel.settings.collectAsState()
    var upMai by remember { mutableStateOf(true) }
    var upLxns by remember { mutableStateOf(settings.lxnsToken.isNotEmpty()) }
    var upWaterfish by remember { mutableStateOf(settings.waterfishToken.isNotEmpty()) }

    val topAppBarScrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())

    // 等待设置加载完成并延迟一秒
    LaunchedEffect(settings) {
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
                        IconButton(
                            onClick = { showSyncSheet.value = true },
                            enabled = false
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Update,
                                contentDescription = null
                            )
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
                    .overScrollVertical()
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
