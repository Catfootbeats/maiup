package xyz.catfootbeats.maiup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        viewModelScope.launch {
            maiupViewModel.settingsState.collect { settings ->
                val token = settings.lxnsToken
                // 只在 token 不为空时执行
                if (token.isNotEmpty()) {
                    loadRatingTrend(token)
                    loadPlayerLxns(token)
                }
            }
        }
    }

    fun reload(token: String){
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
     * 获取Rating趋势
     * @param userToken 用户 Token，用于 API 认证
     */
    private fun loadRatingTrend(userToken: String){
        _error.value = null
        viewModelScope.launch {
            try {
                val response = lxnsApi.getPlayerTrend(userToken)
                if (response.success && response.code == 200) {
                    _lxnsRatingTrend.value = response.data
                } else if (response.code == 401){
                    _error.value = "落雪查分器Token错误"
                } else {
                    _error.value = "获取玩家信息失败: ${response.code}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "获取玩家信息失败"
            }
        }
    }
    /**
     * 获取玩家信息
     * @param userToken 用户 Token，用于 API 认证
     */
    private fun loadPlayerLxns(userToken: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                val response = lxnsApi.getPlayerInfo(userToken)
                if (response.success && response.code == 200) {
                    _lxnsPlayerMaiInfo.value = response.data!!
                } else if (response.code == 401){
                    _error.value = "落雪查分器Token错误"
                } else {
                    _error.value = "获取玩家信息失败: ${response.code}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "获取玩家信息失败"
            } finally {
                _isLoad.value = true
            }
        }
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