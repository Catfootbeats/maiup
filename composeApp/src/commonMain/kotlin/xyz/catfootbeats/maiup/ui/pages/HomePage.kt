package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.WindowDialog
import xyz.catfootbeats.maiup.ui.components.ErrorCard
import xyz.catfootbeats.maiup.ui.components.OthersCard
import xyz.catfootbeats.maiup.ui.components.PasswordTextField
import xyz.catfootbeats.maiup.ui.components.PlayerInfoCardMai
import xyz.catfootbeats.maiup.ui.components.RatingTrendCard
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel

@Composable
fun HomePage() {
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val playerDataViewModel: PlayerDataViewModel = koinViewModel()
    val isLoad by playerDataViewModel.isLoad.collectAsState()
    val playerInfo by playerDataViewModel.lxnsPlayerMaiInfo.collectAsState()
    val ratingTrend by playerDataViewModel.lxnsRatingTrend.collectAsState()
    val dataError by playerDataViewModel.error.collectAsState()

    // 检测lxnsToken是否为空
    val settings by maiupViewModel.settings.collectAsState()
    val showTokenDialog = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        // 延迟一秒后再检测
        delay(1000)
        showTokenDialog.value = settings.lxnsToken.isEmpty()
    }

    // Token输入对话框
    WindowDialog(
        title = "落雪设定",
        summary = null,
        show = showTokenDialog,
        onDismissRequest = {
            if(!settings.lxnsToken.isEmpty()) {
                showTokenDialog.value = false
                playerDataViewModel.reload(settings.lxnsToken)
            }
        } // 关闭对话框
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PasswordTextField(
                value = settings.lxnsToken,
                lable = "输入落雪 API 密钥喵~",
                onValueChange = {
                    maiupViewModel.updateLxnsAPI(it)
                },
            )
            TextButton(
                text = "确定",
                onClick = {
                    if(!settings.lxnsToken.isEmpty()) {
                        showTokenDialog.value = false
                        playerDataViewModel.reload(settings.lxnsToken)
                    }
                }, // 关闭对话框
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        // verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        dataError?.let { ErrorCard(it) }
        if (!isLoad) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                )
            }
        }else {
            // 玩家信息卡片
            PlayerInfoCardMai(
                avatarId = playerInfo.icon.id,
                playerId = playerInfo.name,
                rating = playerInfo.rating,
                syncDate = playerInfo.upload_time
            )
            // Rating趋势图卡片
            RatingTrendCard(ratingTrend)
            // 工具卡片
            OthersCard()
        }
    }
}
