package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.model.LevelIndex
import xyz.catfootbeats.maiup.model.Score

@Composable
fun ScoreCard(score: Score) {
    val backgroundColor = when(score.level_index) {
        LevelIndex.BASIC -> Color(0xFF4CAF50) // Basic - 绿色
        LevelIndex.ADVANCED -> Color(0xFFFFEB3B) // Advanced - 黄色
        LevelIndex.EXPERT -> Color(0xFFF44336) // Expert - 红色
        LevelIndex.MASTER -> Color(0xFF9C27B0) // Master - 紫色
        LevelIndex.ReMASTER -> Color(0xFFE1BEE7) // Re:MASTER - 白紫色
    }

    Card(
        modifier = Modifier
            .width(250.dp)
            .padding(4.dp),
        colors = CardColors(
            color = backgroundColor,
            contentColor = MiuixTheme.colorScheme.onBackground
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // 歌曲名称和类型
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = score.song_name,
                    style = MiuixTheme.textStyles.headline2,
                    fontWeight = FontWeight.Bold,
                    color = if(score.level_index == LevelIndex.MASTER) Color.Black else Color.White
                )
                Text(
                    text = score.type.name,
                    style = MiuixTheme.textStyles.body2,
                    color = if(score.level_index == LevelIndex.MASTER) Color.Black else Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ID和等级
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ID: ${score.id}",
                    style = MiuixTheme.textStyles.body2,
                    color = if(score.level_index == LevelIndex.MASTER) Color.Black else Color.White
                )
                Text(
                    text = "等级: ${score.level}",
                    style = MiuixTheme.textStyles.body2,
                    color = if(score.level_index == LevelIndex.MASTER) Color.Black else Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // DX分数和DX Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "DX分数: ${score.dx_score}",
                    style = MiuixTheme.textStyles.body2,
                    color = if(score.level_index == LevelIndex.MASTER) Color.Black else Color.White
                )
                Text(
                    text = "DX Rating: ${score.dx_rating.toInt()}",
                    style = MiuixTheme.textStyles.body2,
                    color = if(score.level_index == LevelIndex.MASTER) Color.Black else Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 最后游玩时间
            Text(
                text = "最后游玩: ${score.last_played_time}",
                style = MiuixTheme.textStyles.footnote2,
                color = if(score.level_index == LevelIndex.MASTER) Color.Black else Color.White
            )
        }
    }
}