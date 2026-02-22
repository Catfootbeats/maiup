package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import xyz.catfootbeats.maiup.ui.animation.AnimatedItem
import xyz.catfootbeats.maiup.ui.components.ErrorCard
import xyz.catfootbeats.maiup.ui.components.OthersCard
import xyz.catfootbeats.maiup.ui.components.PlayerInfoCardMai
import xyz.catfootbeats.maiup.ui.components.RatingTrendCard
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun HomePage() {
    val playerDataViewModel: PlayerDataViewModel = koinViewModel()
    val isLoad by playerDataViewModel.isLoad.collectAsState()
    val playerInfo by playerDataViewModel.lxnsPlayerMaiInfo.collectAsState()
    val ratingTrend by playerDataViewModel.lxnsRatingTrend.collectAsState()
    val dataError by playerDataViewModel.error.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AnimatedItem(dataError!=null){
                dataError?.let { ErrorCard(it) }
            }
        }
        item {
            if(!isLoad) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            AnimatedItem(isLoad){
                // 玩家信息卡片
                PlayerInfoCardMai(
                    avatarId = playerInfo.icon.id,
                    playerId = playerInfo.name,
                    rating = playerInfo.rating,
                    syncDate = playerInfo.upload_time
                )
            }
        }
        item {
            AnimatedItem(isLoad) {
                // Rating趋势图卡片
                RatingTrendCard(ratingTrend)
            }
        }
        item {
            AnimatedItem(isLoad) {
                // 工具卡片
                OthersCard()
            }
        }
    }
}
