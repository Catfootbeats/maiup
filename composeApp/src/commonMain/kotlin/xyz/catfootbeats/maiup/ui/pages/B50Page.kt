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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalDivider
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.AppRecording
import top.yukonga.miuix.kmp.icon.extended.Blocklist
import top.yukonga.miuix.kmp.icon.extended.File
import top.yukonga.miuix.kmp.icon.extended.ListView
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.model.LevelIndex
import xyz.catfootbeats.maiup.ui.components.ScoreCard
import xyz.catfootbeats.maiup.ui.components.getLevelColor
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun B50Page() {
    val playerData: PlayerDataViewModel = koinViewModel()

    playerData.lxnsBest50.value?.let {
        Column {
            Card(
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("DX 评分总和")
                    val score = it.dx_total + it.standard_total
                    Card(
                        cornerRadius = 32.dp,
                        colors = CardColors(
                            color = when(score){
                                in 0..999 -> Color(0xFF)
                                in 1000..1999 -> Color(0xFF00FF00)
                                in 2000..3999 -> Color(0xFFFF00)
                                in 4000..6999 -> Color(0xFF00FFFF)
                                in 7000..9999 -> Color(0xFF00FF00)
                                in 10000..11999 -> Color(0xFFFF00)
                                in 12000..12999 -> Color(0xFF00FFFF)
                                in 13000..13999 -> Color(0xFF66CCFF)
                                in 14000..14499 -> Color(0xFFFFD700)
                                in 14500..14999 -> Color(0xFFFAF0E6)
                                else -> Color(0xFF14101B)
                            },
                            contentColor = MiuixTheme.colorScheme.onBackground
                        )
                    ){
                        Text(
                            text="  $score  ",
                            modifier=Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 两个子Card，自适应屏幕大小
                    // 使用FlowRow实现响应式布局，小屏幕时自动换行
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // B35 Card
                        Card(
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 256.dp),
                            colors = CardColors(
                                MiuixTheme.colorScheme.primaryContainer,
                                MiuixTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val standardScores = it.standard.map { score -> score.dx_rating }
                                val maxScore = standardScores.maxOrNull() ?: 0
                                val minScore = standardScores.minOrNull() ?: 0
                                val avgScore =
                                    if (standardScores.isNotEmpty()) standardScores.average() else 0
                                Row (verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        MiuixIcons.File,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(16.dp))
                                    Column {
                                        Text("B35", style = MiuixTheme.textStyles.headline2)
                                        Text(
                                            "${it.standard_total}"
                                        )
                                    }
                                }
                                Column {
                                    Text("Max", style = MiuixTheme.textStyles.footnote1)
                                    Text("${maxScore.toInt()}")
                                }
                                Column {
                                    Text("Avg", style = MiuixTheme.textStyles.footnote1)
                                    Text("${avgScore.toInt()}")
                                }
                                Column {
                                    Text("Min", style = MiuixTheme.textStyles.footnote1)
                                    Text("${minScore.toInt()}")
                                }
                            }
                        }
                        // B15 Card
                        Card(
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 256.dp),
                            colors = CardColors(
                                MiuixTheme.colorScheme.primaryContainer,
                                MiuixTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val dxScores = it.dx.map { score -> score.dx_rating }
                                val maxScore = dxScores.maxOrNull() ?: 0
                                val minScore = dxScores.minOrNull() ?: 0
                                val avgScore = if (dxScores.isNotEmpty()) dxScores.average() else 0

                                Row (verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        MiuixIcons.File,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text("B15", style = MiuixTheme.textStyles.headline2)
                                        Text(
                                            "${it.standard_total}"
                                        )
                                    }
                                }
                                Column {
                                    Text("Max", style = MiuixTheme.textStyles.footnote1)
                                    Text("${maxScore.toInt()}")
                                }
                                Column {
                                    Text("Avg", style = MiuixTheme.textStyles.footnote1)
                                    Text("${avgScore.toInt()}")
                                }
                                Column {
                                    Text("Min", style = MiuixTheme.textStyles.footnote1)
                                    Text("${minScore.toInt()}")
                                }
                            }
                        }

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
                it.standard.forEach { b35 ->
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
                it.dx.forEach { b15 ->
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