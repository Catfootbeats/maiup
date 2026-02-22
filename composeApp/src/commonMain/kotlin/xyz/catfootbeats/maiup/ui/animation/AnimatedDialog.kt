package xyz.catfootbeats.maiup.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable

/**
 * 为对话框添加缩放和淡入淡出动画效果
 * @param visible 对话框是否可见
 * @param content 对话框内容
 */
@Composable
fun AnimatedDialog(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            ),
            initialScale = 0.8f
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ),
        exit = scaleOut(
            animationSpec = tween(
                durationMillis = 100,
                easing = FastOutSlowInEasing
            ),
            targetScale = 0.8f
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 100,
                easing = FastOutSlowInEasing
            )
        ),
        label = "AnimatedDialog"
    ) {
        content()
    }
}
