package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.File
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.ui.components.ScoreCard
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

/**
 * 舞萌 DX Rating 各段位的颜色
 */
private fun ratingColor(score: Int): Color = when (score) {
    in 0..999 -> Color(0xFF9E9E9E)      // 灰色
    in 1000..1999 -> Color(0xFF4CAF50)  // 绿
    in 2000..3999 -> Color(0xFFFFEB3B)  // 黄
    in 4000..6999 -> Color(0xFFFF9800)  // 橙
    in 7000..9999 -> Color(0xFFF44336)  // 红
    in 10000..11999 -> Color(0xFF9C27B0)// 紫
    in 12000..12999 -> Color(0xFF8D6E63)// 铜
    in 13000..13999 -> Color(0xFFBDBDBD)// 银
    in 14000..14499 -> Color(0xFFFFD700)// 金
    in 14500..14999 -> Color(0xFFCE93D8)// 彩
    else -> Color(0xFF14101B)            // 极
}

@Composable
private fun StatCard(
    label: String, total: Int, scores: List<Float>, icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    val maxScore = scores.maxOrNull()?.toInt() ?: 0
    val minScore = scores.minOrNull()?.toInt() ?: 0
    val avgScore = if (scores.isNotEmpty()) scores.average().toInt() else 0

    Card(
        modifier = modifier.defaultMinSize(minWidth = 256.dp),
        colors = CardColors(
            MiuixTheme.colorScheme.primaryContainer,
            MiuixTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(label, style = MiuixTheme.textStyles.headline2)
                    Text("$total", style = MiuixTheme.textStyles.body1)
                }
            }
            Column {
                Text("Max", style = MiuixTheme.textStyles.footnote1)
                Text("$maxScore")
            }
            Column {
                Text("Avg", style = MiuixTheme.textStyles.footnote1)
                Text("$avgScore")
            }
            Column {
                Text("Min", style = MiuixTheme.textStyles.footnote1)
                Text("$minScore")
            }
        }
    }
}

@Composable
fun B50Page() {
    val playerData: PlayerDataViewModel = koinViewModel()

    playerData.lxnsBest50.value?.let { data ->
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
            Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("DX 评分总和")
                    val score = data.dx_total + data.standard_total
                    Card(
                        cornerRadius = 32.dp,
                        colors = CardColors(
                            color = ratingColor(score),
                            contentColor = MiuixTheme.colorScheme.onBackground
                        )
                    ) {
                        Text(
                            text = "  $score  ",
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "B35", total = data.standard_total,
                            scores = data.standard.map { it.dx_rating },
                            icon = MiuixIcons.File
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "B15", total = data.dx_total,
                            scores = data.dx.map { it.dx_rating },
                            icon = MiuixIcons.File
                        )
                    }
                }
            }

            SmallTitle("B35")

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 5
            ) {
                data.standard.forEach { b35 ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(230.dp)
                            .padding(4.dp),
                    ) {
                        ScoreCard(b35)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            SmallTitle("B15")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 5
            ) {
                data.dx.forEach { b15 ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(230.dp)
                            .padding(4.dp),
                    ) {
                        ScoreCard(b15)
                    }
                }
            }
        }
    }
}
