package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
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
    Card {
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
                    style = MiuixTheme.textStyles.title2
                )
                Text(
                    text = "Rating: $rating",
                    style = MiuixTheme.textStyles.body1,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "同步日期: ${convertUtcToPlus8(syncDate)}",
                    style =  MiuixTheme.textStyles.body2,
                )
            }
        }
    }
}
