package xyz.catfootbeats.maiup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.api.LxnsApi
import xyz.catfootbeats.maiup.model.ApiResponse

class RankViewModel(
    private val lxnsApi: LxnsApi,
    private val maiupViewModel: MaiupViewModel
) : ViewModel() {
    private val _isLoad = MutableStateFlow(false)
    val isLoad: StateFlow<Boolean> = _isLoad.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var retryJob: Job? = null

    init {
        viewModelScope.launch {
            maiupViewModel.settings.collect { settings ->
                val token = settings.lxnsToken
                reload(token)
            }
        }
    }

    fun reload(token: String) {
        // 取消之前的重试任务
        retryJob?.cancel()
        viewModelScope.launch {
            // 只在 token 不为空时执行
            if (token.isNotEmpty()) {
                _isLoad.value = false
            }
        }
    }

    /**
     * 通用API请求处理函数
     * @param userToken 用户 Token
     * @param apiCall API调用函数
     * @param onSuccess 成功回调
     * @param onFailed 失败回调
     * @param onRetry 重试回调
     */
    private fun <T> handleApiCall(
        userToken: String,
        apiCall: suspend (String) -> ApiResponse<T>,
        onSuccess: (ApiResponse<T>) -> Unit,
        onFailed: (ApiResponse<T>) -> Unit = {},
        onRetry: suspend (String) -> ApiResponse<T>
    ) {
        _error.value = null
        viewModelScope.launch {
            try {
                val response = apiCall(userToken)
                when {
                    response.success && response.code == 200 -> onSuccess(response)
                    response.code == 401 -> {
                        _error.value = "落雪查分器 Token 错误喵"
                        onFailed(response)
                    }
                    else -> {
                        _error.value = "获取玩家信息失败: ${response.code}喵"
                        onFailed(response)
                    }
                }
            } catch (e: Exception) {
                if (e.message?.contains("No address associated with hostname") == true) {
                    _error.value = "你没联网喵~"
                } else {
                    _error.value = e.message ?: "请求失败喵"
                }
                // 失败后每隔5秒重试
                retryJob = viewModelScope.launch {
                    while (true) {
                        delay(5000)
                        try {
                            val response = onRetry(userToken)
                            onSuccess(response)
                            _error.value = null
                            break
                        } catch (_: Exception) {
                            // 继续重试
                        }
                    }
                }
            }
        }
    }
}