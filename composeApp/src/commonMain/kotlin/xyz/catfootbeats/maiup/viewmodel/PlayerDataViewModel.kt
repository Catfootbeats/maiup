package xyz.catfootbeats.maiup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.api.LxnsApi
import xyz.catfootbeats.maiup.model.LxnsPlayerMai

class PlayerDataViewModel(
    private val lxnsApi: LxnsApi
): ViewModel() {

    private val _Lxns_playerMaiInfo = MutableStateFlow<LxnsPlayerMai>(LxnsPlayerMai())
    val lxnsPlayerMaiInfo: StateFlow<LxnsPlayerMai> = _Lxns_playerMaiInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * 获取玩家信息
     * @param userToken 用户 Token，用于 API 认证
     */
    fun loadPlayerLxns(userToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = lxnsApi.getPlayerInfoMai(userToken)
                if (response.success && response.code == 200) {
                    _Lxns_playerMaiInfo.value = response.data!!
                } else if (response.code == 401){
                    _error.value = "落雪查分器Token错误"
                } else {
                    _error.value = "获取玩家信息失败: ${response.code}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "获取玩家信息失败"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 上传分数
     */
    fun uploadScoreLxns() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val currentPlayer = _Lxns_playerMaiInfo.value
                if (currentPlayer != null) {
                    val success = false//playerApi.uploadPlayerInfo(currentPlayer)
                    if (!success) {
                        _error.value = "上传分数失败"
                    }
                } else {
                    _error.value = "没有可上传的玩家信息"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "上传分数失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
}