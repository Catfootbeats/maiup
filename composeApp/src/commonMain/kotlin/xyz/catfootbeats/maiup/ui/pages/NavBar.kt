package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.*
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
 import xyz.catfootbeats.maiup.ui.pages.LoginPage
 import xyz.catfootbeats.maiup.resources.Res
 import xyz.catfootbeats.maiup.resources.mai
 import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val desc: String,
) {
    HOME("首页", MiuixIcons.Contacts, "账号详情"),
    SYNC("同步", MiuixIcons.Update, "数据同步"),
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

     val settings by maiupViewModel.settings.collectAsState()
     val isSettingsLoaded by maiupViewModel.isSettingsLoaded.collectAsState()

     if (!isSettingsLoaded) {
         Box(
             modifier = Modifier.fillMaxSize(),
             contentAlignment = Alignment.Center
         ) {
             Column(horizontalAlignment = Alignment.CenterHorizontally) {
                 Image(
                     painter = painterResource(Res.drawable.mai),
                     contentDescription = "MaiUp",
                     modifier = Modifier.size(96.dp)
                 )
                 Spacer(Modifier.height(16.dp))
                 Text(
                     text = "MaiUp!!!",
                     fontSize = 28.sp,
                     fontWeight = FontWeight.Bold,
                     color = MiuixTheme.colorScheme.onBackground
                 )
             }
         }
         return
     }

     if (!settings.isAuthorized) {
         LoginPage()
         return
     }

    val topAppBarScrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())

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
                     actions = { Spacer(modifier = Modifier.width(16.dp)) }
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
                        AppDestinations.SYNC -> SyncPage()
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
