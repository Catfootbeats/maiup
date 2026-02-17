package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import xyz.catfootbeats.maiup.resources.Res
import xyz.catfootbeats.maiup.resources.mai
import xyz.catfootbeats.maiup.utils.openUrl
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun HomePage() {
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val playerDataViewModel: PlayerDataViewModel = koinViewModel()
    val playerInfo by playerDataViewModel.lxnsPlayerMaiInfo.collectAsState()
    val dataError by playerDataViewModel.error.collectAsState()
    val settings by maiupViewModel.settingsState.collectAsState()

    // 只在 lxnsToken 有值时才加载数据
    LaunchedEffect(settings.lxnsToken) {
        if (settings.lxnsToken.isNotEmpty()) {
            playerDataViewModel.loadPlayerLxns(settings.lxnsToken)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        dataError?.let {
            item {
                ErrorInfoCard(it,"加载数据错误")
            }
        }
        item {
            // 玩家信息卡片
            PlayerInfoCardMai(
                avatar = Res.drawable.mai, // 替换为实际头像URL
                playerId = playerInfo.name,
                rating = playerInfo.rating,
                syncDate = playerInfo.upload_time
            )
        }
        item {
            // Rating趋势图卡片
            RatingTrendCard()
        }
        item {
            // Rating趋势图卡片
            OthersCard()
        }
    }
}

@Composable
fun PlayerInfoCardMai(
    avatar: DrawableResource,
    playerId: String,
    rating: Int,
    syncDate: String
) {
    Card(
        modifier = Modifier
            .width(500.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            //contentColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 玩家头像
            Image(
                painter = painterResource(avatar),
                contentDescription = "头像",
                modifier = Modifier.size(100.dp)
            )
            // 玩家信息
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = playerId,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Rating: $rating",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "同步日期: $syncDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RatingTrendCard() {
    Card(
        modifier = Modifier
            .width(500.dp)
            .height(300.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
           // contentColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Rating 趋势",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 这里可以集成实际的图表库，如 Victor 或 Compose Charts
            // 临时用占位符显示
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Rating 趋势图区域",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun OthersCard() {
    Card(
        modifier = Modifier
            .width(500.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            // contentColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "其他",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val iconItems = remember {
                    listOf(
                        IconItemData(
                            icon = Icons.Default.Map,
                            text = "音游地图",
                            webUrl = "https://map.bemanicn.com/"
                        ),
                        IconItemData(
                            icon = Icons.Default.Api,
                            text = "落雪查分器",
                            webUrl = "https://maimai.lxns.net/"
                        ),
                        IconItemData(
                            icon = Icons.Default.Api,
                            text = "水鱼查分器",
                            webUrl = "https://www.diving-fish.com/maimaidx/prober/"
                        ),
                        IconItemData(
                            icon = Icons.Default.Api,
                            text = "DXRating",
                            webUrl = "https://dxrating.net/"
                        ),
                        IconItemData(
                            icon = Icons.Default.Api,
                            text = "Union",
                            webUrl = "https://union.godserver.cn/"
                        ),
                    )
                }

                iconItems.forEach { item ->
                    IconGridItem(
                        icon = item.icon,
                        text = item.text,
                        webUrl = item.webUrl,
                        openExternal = item.openExternal
                    )
                }
            }
        }
    }
}

data class IconItemData(
    val icon: ImageVector,
    val text: String,
    val webUrl: String,
    val openExternal: Boolean = true
)

@Composable
fun IconGridItem(
    icon: ImageVector,
    text: String,
    webUrl: String,
    openExternal: Boolean = true
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(16 .dp))
            .clickable{
                if(openExternal){
                    openUrl(webUrl)
                }
            }
            .size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ErrorInfoCard(
    msg: String,
    title: String = "错误"
) {
    Card(
        modifier = Modifier
            .width(500.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SelectionContainer {
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
