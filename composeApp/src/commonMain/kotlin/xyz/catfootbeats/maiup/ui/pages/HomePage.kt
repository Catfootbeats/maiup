package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import xyz.catfootbeats.maiup.model.RatingTrend
import xyz.catfootbeats.maiup.resources.Res
import xyz.catfootbeats.maiup.resources.default_avatar
import xyz.catfootbeats.maiup.utils.convertUtcToPlus8
import xyz.catfootbeats.maiup.utils.openUrl
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun HomePage() {
    val playerDataViewModel: PlayerDataViewModel = koinViewModel()
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val settings by maiupViewModel.settingsState.collectAsState()
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
            Anima(dataError!=null){
                dataError?.let { ErrorInfoCard(it) }
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
            Anima(isLoad){
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
            Anima(isLoad) {
                // Rating趋势图卡片
                RatingTrendCard(ratingTrend)
            }
        }
        item {
            Anima(isLoad) {
                // 工具卡片
                OthersCard()
            }
        }
    }
}

/**
 * 一个带有动画效果的Composable函数，根据isLoad参数控制内容的显示与隐藏
 * @param isLoad 布尔值，控制内容是否显示
 * @param content 当isLoad为true时显示的Composable内容
 */
@Composable
fun Anima(isLoad: Boolean,content: @Composable () -> Unit){
    // 使用AnimatedVisibility实现动画效果
    AnimatedVisibility(
        visible = isLoad, // 根据isLoad参数控制显示状态

        // 定义进入动画
        enter = fadeIn( // 淡入效果
            animationSpec = tween(500, delayMillis = 100) // 设置动画持续时间为500毫秒，延迟100毫秒
        ) + slideInVertically( // 垂直滑入效果
            animationSpec = tween(500), // 设置动画持续时间为500毫秒
            initialOffsetY = { it / 2 } // 初始垂直偏移量为自身高度的一半
        )
    ) {
        content() // 当isLoad为true时，显示传入的内容
    }
}

@Composable
fun PlayerInfoCardMai(
    avatarId: Int,
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
            AsyncImage(
                model = "https://assets2.lxns.net/maimai/icon/$avatarId.png",
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                error = painterResource(Res.drawable.default_avatar)
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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "同步日期: ${convertUtcToPlus8(syncDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RatingTrendCard(ratingTrendList:List<RatingTrend>?) {
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if(ratingTrendList!=null) {
                    LineChart(
                        modifier = Modifier.fillMaxSize(),
                        data = remember(ratingTrendList) {
                            listOf(
                                Line(
                                    values = ratingTrendList.map { it.total.toDouble() },
                                    color = SolidColor(Color(0xFF23af92)),
                                    firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                                    secondGradientFillColor = Color.Transparent,
                                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                                    gradientAnimationDelay = 1000,
                                    drawStyle = DrawStyle.Stroke(width = 2.dp), // 线条粗细
                                )
                            )
                        },
                        indicatorProperties = HorizontalIndicatorProperties(
                            textStyle = MaterialTheme.typography.labelSmall
                                .copy(color = MaterialTheme.colorScheme.onBackground),
                            position = IndicatorPosition.Horizontal.End,
                            contentBuilder = { indicator ->
                                indicator.toInt().toString()
                            },
                            padding = 4.dp
                        ),
                        labelProperties = LabelProperties(
                            enabled = true,
                            textStyle = MaterialTheme.typography.labelSmall
                                .copy(color = MaterialTheme.colorScheme.onBackground),
                            labels = ratingTrendList
                                .filterIndexed { index, _ -> index % ratingTrendList.size/4 == 0 }
                                .map { it.date },
                            padding = 4.dp
                        ),
                        maxValue = ratingTrendList.map { it.total.toDouble() }.max()+100.0,
                        minValue = if((ratingTrendList.map { it.total.toDouble() }.min()-100.0)<0)
                        {0.0}
                        else{
                            ratingTrendList.map { it.total.toDouble() }.min()-100.0
                        },
                        animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
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
