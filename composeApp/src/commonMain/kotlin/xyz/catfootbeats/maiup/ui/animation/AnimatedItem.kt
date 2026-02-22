package xyz.catfootbeats.maiup.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable

/**
 * 一个带有动画效果的Composable函数，根据isLoad参数控制内容的显示与隐藏
 * @param isLoad 布尔值，控制内容是否显示
 * @param content 当isLoad为true时显示的Composable内容
 */
@Composable
fun AnimatedItem(isLoad: Boolean, content: @Composable () -> Unit){
    // 使用AnimatedVisibility实现动画效果
    AnimatedVisibility(
        visible = isLoad, // 根据isLoad参数控制显示状态

        // 定义进入动画
        enter = fadeIn( // 淡入效果
            animationSpec = tween(500, delayMillis = 100) // 设置动画持续时间为500毫秒，延迟100毫秒
        ) + slideInVertically( // 垂直滑入效果
            animationSpec = tween(500), // 设置动画持续时间为500毫秒
            initialOffsetY = { it / 2 } // 初始垂直偏移量为自身高度的一半
        )
    ) {
        content() // 当isLoad为true时，显示传入的内容
    }
}
