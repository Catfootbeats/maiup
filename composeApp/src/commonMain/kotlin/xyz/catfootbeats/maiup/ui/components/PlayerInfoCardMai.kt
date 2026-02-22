package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import xyz.catfootbeats.maiup.resources.Res
import xyz.catfootbeats.maiup.resources.default_avatar
import xyz.catfootbeats.maiup.utils.convertUtcToPlus8

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
