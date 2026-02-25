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
import xyz.catfootbeats.maiup.model.LxnsPlayerMai
import xyz.catfootbeats.maiup.model.RatingTrend

class PlayerDataViewModel(
    private val lxnsApi: LxnsApi,
    private val maiupViewModel: MaiupViewModel
): ViewModel() {
    private val _lxnsPlayerMaiInfo = MutableStateFlow(LxnsPlayerMai())
    val lxnsPlayerMaiInfo: StateFlow<LxnsPlayerMai> = _lxnsPlayerMaiInfo.asStateFlow()
    private val _lxnsRatingTrend = MutableStateFlow<List<RatingTrend>?>(null)
    val lxnsRatingTrend: StateFlow<List<RatingTrend>?> = _lxnsRatingTrend.asStateFlow()
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

    fun reload(token: String){
        // 取消之前的重试任务
        retryJob?.cancel()
        viewModelScope.launch {
            // 只在 token 不为空时执行
            if (token.isNotEmpty()) {
                _isLoad.value = false
                loadRatingTrend(token)
                loadPlayerLxns(token)
            }
        }
    }

    /**
     * 通用API请求处理函数
     * @param userToken 用户 Token
     * @param apiCall API调用函数
     * @param onSuccess 成功回调
     * @param onError 错误回调
     * @param onRetry 重试回调
     */
    private fun <T> handleApiCall(
        userToken: String,
        apiCall: suspend (String) -> T,
        onSuccess: (T) -> Unit,
        onRetry: suspend (String) -> T
    ) {
        _error.value = null
        viewModelScope.launch {
            try {
                val response = apiCall(userToken)
                onSuccess(response)
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

    /**
     * 获取Rating趋势
     * @param userToken 用户 Token，用于 API 认证
     */
    private fun loadRatingTrend(userToken: String) {
        handleApiCall(
            userToken = userToken,
            apiCall = { lxnsApi.getPlayerTrend(it) },
            onSuccess = { response ->
                when {
                    response.success && response.code == 200 -> {
                        _lxnsRatingTrend.value = response.data
                    }
                    response.code == 401 -> {
                        _error.value = "落雪查分器 Token 错误喵"
                    }
                    else -> {
                        _error.value = "获取玩家信息失败: ${response.code}喵"
                    }
                }
            },
            onRetry = { lxnsApi.getPlayerTrend(it) }
        )
    }

    /**
     * 获取玩家信息
     * @param userToken 用户 Token，用于 API 认证
     */
    private fun loadPlayerLxns(userToken: String) {
        handleApiCall(
            userToken = userToken,
            apiCall = { lxnsApi.getPlayerInfo(it) },
            onSuccess = { response ->
                when {
                    response.success && response.code == 200 -> {
                        _lxnsPlayerMaiInfo.value = response.data!!
                    }
                    response.code == 401 -> {
                        _error.value = "落雪查分器Token错误"
                    }
                    else -> {
                        _error.value = "获取玩家信息失败: ${response.code}"
                    }
                }
                _isLoad.value = true
            },
            onRetry = { lxnsApi.getPlayerInfo(it) }
        )
    }

    /**
     * 上传分数
     */
    fun uploadScoreLxns() {
        viewModelScope.launch {
            _error.value = null
            try {
                val currentPlayer = _lxnsPlayerMaiInfo.value
                val success = false//playerApi.uploadPlayerInfo(currentPlayer)
                if (!success) {
                    _error.value = "上传分数失败"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "上传分数失败"
            }finally {
                _isLoad.value = true
            }
        }
    }
}