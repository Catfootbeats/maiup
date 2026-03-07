package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.ui.components.ScoreCard
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun B50Page(){
    val playerData: PlayerDataViewModel = koinViewModel()

    playerData.lxnsBest50.value?.let {
        Column{
            Card(
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("DX 评分总和")
                        Text(
                            "${it.dx_total + it.standard_total}"
                        )
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
                            modifier = Modifier.weight(1f),
                            colors = CardColors(
                                MiuixTheme.colorScheme.background,
                                MiuixTheme.colorScheme.onBackground
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                    Text(
                                        "旧曲 ${it.standard_total}"
                                    )

                                    val standardScores = it.standard.map { score -> score.dx_rating }
                                    val maxScore = standardScores.maxOrNull() ?: 0
                                    val minScore = standardScores.minOrNull() ?: 0
                                    val avgScore =
                                        if (standardScores.isNotEmpty()) standardScores.average().toInt() else 0

                                    //Text("MAX: $maxScore")
                                    //Text("AVG: $avgScore")
                                    //Text("MIN: $minScore")

                            }
                        }

                        // B15 Card
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardColors(
                                MiuixTheme.colorScheme.background,
                                MiuixTheme.colorScheme.onBackground
                            )

                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                    Text(
                                        "新曲 ${it.dx_total}"
                                    )

                                    val dxScores = it.dx.map { score -> score.dx_rating }
                                    val maxScore = dxScores.maxOrNull() ?: 0
                                    val minScore = dxScores.minOrNull() ?: 0
                                    val avgScore = if (dxScores.isNotEmpty()) dxScores.average().toInt() else 0

                                    //Text("MAX: $maxScore")
                                    //Text("AVG: $avgScore")
                                    //Text("MIN: $minScore")
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
                    ScoreCard(b35)
                }
            }

            SmallTitle("B15")

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 5
            ) {
                it.dx.forEach { b15 ->
                    ScoreCard(b15)
                }
            }
        }
    }
}