package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.ui.components.ErrorCard
import xyz.catfootbeats.maiup.ui.components.OthersCard
import xyz.catfootbeats.maiup.ui.components.PlayerInfoCardMai
import xyz.catfootbeats.maiup.ui.components.RatingTrendCard
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun HomePage() {
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val playerDataViewModel: PlayerDataViewModel = koinViewModel()
    val hasData by playerDataViewModel.hasData.collectAsState()
    val isRefreshing by playerDataViewModel.isRefreshing.collectAsState()
    val playerInfo by playerDataViewModel.lxnsPlayerMaiInfo.collectAsState()
    val ratingTrend by playerDataViewModel.lxnsRatingTrend.collectAsState()
    val dataError by playerDataViewModel.error.collectAsState()
    val settings by maiupViewModel.settings.collectAsState()

     LaunchedEffect(settings.isAuthorized) {
         if (settings.isAuthorized) {
             val token = maiupViewModel.tryRefreshToken()
             playerDataViewModel.reload(token)
         }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        if (!hasData && !isRefreshing) {
            if (!settings.isAuthorized) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "请到设置页面完成 OAuth 授权",
                        style = MiuixTheme.textStyles.body1,
                        color = MiuixTheme.colorScheme.onBackground
                    )
                }
            }
        } else if (!hasData) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        } else {
            dataError?.let { ErrorCard(it) }
            PlayerInfoCardMai(
                avatarId = playerInfo.icon.id,
                playerId = playerInfo.name,
                rating = playerInfo.rating,
                syncDate = playerInfo.upload_time
            )
            RatingTrendCard(ratingTrend)
            OthersCard()
        }
    }
}
