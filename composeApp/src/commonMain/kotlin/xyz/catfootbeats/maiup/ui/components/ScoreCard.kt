package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.model.LevelIndex
import xyz.catfootbeats.maiup.model.Score
import xyz.catfootbeats.maiup.resources.Res
import xyz.catfootbeats.maiup.resources.default_avatar

fun getLevelColor(levelIndex: LevelIndex): Color {
    return when (levelIndex) {
        LevelIndex.BASIC -> Color(0xFF4CAF50) // Basic - 绿色
        LevelIndex.ADVANCED -> Color(0xFFFFEB3B) // Advanced - 黄色
        LevelIndex.EXPERT -> Color(0xFFF44336) // Expert - 红色
        LevelIndex.MASTER -> Color(0xFF9C27B0) // Master - 紫色
        LevelIndex.ReMASTER -> Color(0xFFE1BEE7) // Re:MASTER - 白紫色
    }
}

@Composable
fun ScoreCard(score: Score) {
    Box(Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.matchParentSize(), // 使用 matchParentSize 确保填满,
            model = "https://assets2.lxns.net/maimai/jacket/${score.id}.png",
            contentDescription = null,
            contentScale = ContentScale.Crop, // 添加这个确保图片裁剪填满
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            getLevelColor(score.level_index).copy(alpha = 0.7f)
                        ),
                        startY = 0.7f,  // 从50%位置开始
                        endY = 1.0f     // 到100%位置结束
                    )
                )
        )
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ID: ${score.id}",
                        style = MiuixTheme.textStyles.body2,
                        color = if (score.level_index == LevelIndex.ReMASTER) Color.Black else Color.White
                    )
                    Text(
                        text = score.type.name,
                        style = MiuixTheme.textStyles.body2,
                        color = if (score.level_index == LevelIndex.ReMASTER) Color.Black else Color.White
                    )
                }
                Text(
                    text = score.song_name,
                    style = MiuixTheme.textStyles.headline2,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = if (score.level_index == LevelIndex.ReMASTER) Color.Black else Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "等级: ${score.level}",
                    style = MiuixTheme.textStyles.body2,
                    color = if (score.level_index == LevelIndex.ReMASTER) Color.Black else Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "DX Rating: ${score.dx_rating.toInt()}",
                    style = MiuixTheme.textStyles.body2,
                    color = if (score.level_index == LevelIndex.ReMASTER) Color.Black else Color.White
                )
            }
        }
    }
}