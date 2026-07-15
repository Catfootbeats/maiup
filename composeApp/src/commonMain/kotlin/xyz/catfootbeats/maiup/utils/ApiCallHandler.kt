package xyz.catfootbeats.maiup.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import xyz.catfootbeats.maiup.model.ApiResponse
import kotlin.time.Duration.Companion.milliseconds

class ApiCallHandler(
    private val scope: CoroutineScope,
    private val maxRetries: Int = 3,
    private val baseRetryMs: Long = 1000,
) {
    private val activeJobs = mutableSetOf<Job>()

    fun <T> call(
        apiCall: suspend () -> ApiResponse<T>,
        onSuccess: (T?) -> Unit,
        onError: (String) -> Unit,
    ) {
        scope.launch {
            try {
                val response = apiCall()
                when {
                    response.success && response.code == 200 -> onSuccess(response.data)
                    response.code == 401 -> onError("授权已过期，请在设置页面清除授权后重新授权")
                    response.code == 429 -> {
                        onError("请求过于频繁，稍后自动重试")
                        retryWithLimit(apiCall, onSuccess, onError, maxRetries)
                    }
                    else -> onError("获取玩家信息失败: ${response.code}喵")
                }
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                onError(formatError(e))
                retryWithLimit(apiCall, onSuccess, onError, maxRetries)
            } finally {
                coroutineContext[Job]?.let { activeJobs.remove(it) }
            }
        }.also { activeJobs.add(it) }
    }

    fun cancelAll() {
        activeJobs.toList().forEach { it.cancel() }
        activeJobs.clear()
    }

    private fun <T> retryWithLimit(
        apiCall: suspend () -> ApiResponse<T>,
        onSuccess: (T?) -> Unit,
        onError: (String) -> Unit,
        remaining: Int,
    ) {
        if (remaining <= 0) return
        val attempt = maxRetries - remaining + 1
        val delayMs = min(baseRetryMs * (1L shl (attempt - 1)), 10000L)
        scope.launch {
            delay(delayMs.milliseconds)
            try {
                val response = apiCall()
                when {
                    response.success && response.code == 200 -> onSuccess(response.data)
                    response.code == 429 -> {
                        onError("请求过于频繁，稍后自动重试")
                        retryWithLimit(apiCall, onSuccess, onError, remaining - 1)
                    }
                    else -> onError("获取玩家信息失败: ${response.code}喵")
                }
            } catch (_: CancellationException) {
            } catch (_: Exception) {
                retryWithLimit(apiCall, onSuccess, onError, remaining - 1)
            } finally {
                coroutineContext[Job]?.let { activeJobs.remove(it) }
            }
        }.also { activeJobs.add(it) }
    }

    private fun formatError(e: Exception): String {
        return if (e.message?.contains("No address associated with hostname") == true) {
            "你没联网喵~"
        } else if (e.message?.contains("No route to host") == true ||
                   e.message?.contains("Network is unreachable") == true) {
            "无法连接到服务器，请检查网络设置"
        } else if (e.message?.contains("Connection refused") == true) {
            "服务器拒绝连接，可能正在维护"
        } else if (e.message?.contains("timeout") == true ||
                   e.message?.contains("timed out") == true) {
            "连接超时，请稍后重试"
        } else {
            e.message ?: "请求失败喵"
        }
    }
}
