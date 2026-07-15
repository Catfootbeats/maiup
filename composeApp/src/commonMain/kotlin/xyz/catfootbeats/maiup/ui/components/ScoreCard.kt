package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.model.FCType
import xyz.catfootbeats.maiup.model.FSType
import xyz.catfootbeats.maiup.model.LevelIndex
import xyz.catfootbeats.maiup.model.Score
import xyz.catfootbeats.maiup.utils.convertUtcToPlus8

fun getLevelColor(levelIndex: LevelIndex): Color {
    return when (levelIndex) {
        LevelIndex.BASIC -> Color(0xFF4CAF50)
        LevelIndex.ADVANCED -> Color(0xFFFFEB3B)
        LevelIndex.EXPERT -> Color(0xFFF44336)
        LevelIndex.MASTER -> Color(0xFF9C27B0)
        LevelIndex.ReMASTER -> Color(0xFFE1BEE7)
    }
}

private fun LevelIndex.label(): String = when (this) {
    LevelIndex.BASIC -> "BASIC"
    LevelIndex.ADVANCED -> "ADVANCED"
    LevelIndex.EXPERT -> "EXPERT"
    LevelIndex.MASTER -> "MASTER"
    LevelIndex.ReMASTER -> "Re:MASTER"
}

private fun FCType?.label(): String = when (this) {
    FCType.FC -> "FC"
    FCType.FCP -> "FC+"
    FCType.AP -> "AP"
    FCType.APP -> "AP+"
    null -> ""
}

private fun FSType?.label(): String = when (this) {
    FSType.SYNC -> "SYNC"
    FSType.FS -> "FS"
    FSType.FSP -> "FS+"
    FSType.FSD -> "FDX"
    FSType.FSDP -> "FDX+"
    null -> ""
}

private fun achievementsColor(ach: Float): Long = when {
    ach >= 100.5f -> 0xFF9C27B0
    ach >= 100.0f -> 0xFFFFD700
    ach >= 99.5f -> 0xFFBDBDBD
    ach >= 99.0f -> 0xFF8D6E63
    ach >= 98.0f -> 0xFFF44336
    ach >= 97.0f -> 0xFFFF9800
    ach >= 94.0f -> 0xFF4CAF50
    ach >= 90.0f -> 0xFF2196F3
    ach >= 80.0f -> 0xFF9E9E9E
    else -> 0xFF607D8B
}

/**
 * 成绩卡片，支持两种模式：
 * - 默认（B50 模式）：全幅曲绘背景 + 渐变遮罩
 * - 紧凑模式：水平布局，左侧缩略图 + 右侧信息
 */
@Composable
fun ScoreCard(
    score: Score,
    levelValue: Float? = null,
    modifier: Modifier = Modifier,
) {
    CompactScoreCard(score, levelValue, modifier)
}
@Composable
private fun CompactScoreCard(
    score: Score,
    levelValue: Float?,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize().height(80.dp)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            getLevelColor(score.level_index).copy(alpha = 0.7f)
                        ),
                        startY = 0.7f,
                        endY = 1.0f
                    )
                )
        )
        Row(
            modifier = modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 曲绘缩略图
            AsyncImage(
                model = "https://assets2.lxns.net/maimai/jacket/${score.id}.png",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(12.dp))

            // 信息区
            Column(modifier = Modifier.weight(1f)) {
                // 第一行：曲名 + 类型标签
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = score.song_name,
                        style = MiuixTheme.textStyles.body1,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        color = Color.White
                    )
                    Text(
                        text = score.type.name,
                        style = MiuixTheme.textStyles.footnote1,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(4.dp))

                // 第二行：难度 + 定数 + 达成率
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${score.level_index.label()} ${score.level}",
                        style = MiuixTheme.textStyles.footnote1,
                        color = Color.White
                    )
                    if (levelValue != null) {
                        Text(
                            text = "定数: $levelValue",
                            style = MiuixTheme.textStyles.footnote1,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "${(score.achievements * 10000).toInt() / 10000.0}%",
                        style = MiuixTheme.textStyles.footnote1,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(4.dp))

                // 第三行：DX 分数 + DX 星 + FC/FS + 上传时间
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DX: ${score.dx_score}",
                            style = MiuixTheme.textStyles.footnote2,
                            color = Color.White
                        )
                        if (score.dx_star > 0) {
                            Text(
                                text = "★".repeat(score.dx_star),
                                style = MiuixTheme.textStyles.footnote2,
                                color = Color(0xFFFF9800)
                            )
                        }
                        if (score.fc != null) {
                            Text(
                                text = score.fc.label(),
                                style = MiuixTheme.textStyles.footnote2,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (score.fs != null) {
                            Text(
                                text = score.fs.label(),
                                style = MiuixTheme.textStyles.footnote2,
                                color = Color(0xFF2196F3),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

}
