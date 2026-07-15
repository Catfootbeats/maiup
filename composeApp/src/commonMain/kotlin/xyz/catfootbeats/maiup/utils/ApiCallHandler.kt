package xyz.catfootbeats.maiup.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.model.ApiResponse

/**
 * 共享的 API 调用处理器，提供统一的重试和错误处理。
 * 每个 [call] 独立管理自己的重试 Job，不会互相干扰。
 */
class ApiCallHandler(
    private val scope: CoroutineScope,
    private val maxRetries: Int = 3,
    private val retryDelayMs: Long = 5000
) {
    private var retryJob: Job? = null

    /**
     * 发起一次 API 调用，失败后自动重试（最多 [maxRetries] 次）。
     * 每次调用 [call] 会取消前一次未完成的重试。
     */
    fun <T> call(
        apiCall: suspend () -> ApiResponse<T>,
        onSuccess: (T?) -> Unit,
        onError: (String) -> Unit,
    ) {
        retryJob?.cancel()
        scope.launch {
            try {
                val response = apiCall()
                when {
                    response.success && response.code == 200 -> onSuccess(response.data)
                    response.code == 401 -> onError("落雪查分器 Token 错误喵")
                    else -> onError("获取玩家信息失败: ${response.code}喵")
                }
            } catch (e: Exception) {
                onError(formatError(e))
                retryWithLimit(apiCall, onSuccess, onError, maxRetries)
            }
        }
    }

    fun cancelRetry() {
        retryJob?.cancel()
    }

    private fun <T> retryWithLimit(
        apiCall: suspend () -> ApiResponse<T>,
        onSuccess: (T?) -> Unit,
        onError: (String) -> Unit,
        remaining: Int,
    ) {
        if (remaining <= 0) return
        retryJob = scope.launch {
            delay(retryDelayMs)
            try {
                val response = apiCall()
                if (response.success && response.code == 200) {
                    onSuccess(response.data)
                }
            } catch (_: Exception) {
                retryWithLimit(apiCall, onSuccess, onError, remaining - 1)
            }
        }
    }

    private fun formatError(e: Exception): String {
        return if (e.message?.contains("No address associated with hostname") == true) {
            "你没联网喵~"
        } else {
            e.message ?: "请求失败喵"
        }
    }
}
