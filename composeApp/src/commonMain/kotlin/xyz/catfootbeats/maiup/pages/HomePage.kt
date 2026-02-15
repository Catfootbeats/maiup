package xyz.catfootbeats.maiup.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun HomePage() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            // 玩家信息卡片
            PlayerInfoCard(
                avatarUrl = "https://example.com/avatar.png", // 替换为实际头像URL
                playerId = "Player_123",
                rating = 114514,
                syncDate = "1970-01-01 00:00"
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
fun PlayerInfoCard(
    avatarUrl: String,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 玩家头像
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = playerId.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // 玩家信息
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "玩家ID: $playerId",
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
            .height(200.dp)
            .width(500.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            // contentColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

            val filters = listOf(
                "Washer/Dryer", "Ramp access", "Garden", "Cats OK", "Dogs OK", "Smoke-free"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { title ->
                    var selected by remember { mutableStateOf(false) }
                    val leadingIcon: @Composable () -> Unit = { Icon(Icons.Default.Check, null) }
                    FilterChip(
                        selected,
                        onClick = { selected = !selected },
                        label = { Text(title) },
                        leadingIcon = if (selected) leadingIcon else null
                    )
                }
            }
        }
    }
}
