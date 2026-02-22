package xyz.catfootbeats.maiup.ui.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import xyz.catfootbeats.maiup.model.RatingTrend

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
