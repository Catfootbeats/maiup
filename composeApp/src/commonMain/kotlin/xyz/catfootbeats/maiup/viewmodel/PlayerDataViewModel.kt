package xyz.catfootbeats.maiup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import xyz.catfootbeats.maiup.api.LxnsApi
import xyz.catfootbeats.maiup.model.Best50
import xyz.catfootbeats.maiup.model.LxnsPlayerMai
import xyz.catfootbeats.maiup.model.RatingTrend
import xyz.catfootbeats.maiup.utils.ApiCallHandler

class PlayerDataViewModel(
    private val lxnsApi: LxnsApi,
) : ViewModel() {
    private val apiHandler = ApiCallHandler(viewModelScope)

    private val _lxnsPlayerMaiInfo = MutableStateFlow(LxnsPlayerMai())
    val lxnsPlayerMaiInfo: StateFlow<LxnsPlayerMai> = _lxnsPlayerMaiInfo.asStateFlow()

    private val _lxnsRatingTrend = MutableStateFlow<List<RatingTrend>?>(null)
    val lxnsRatingTrend: StateFlow<List<RatingTrend>?> = _lxnsRatingTrend.asStateFlow()

    private val _lxnsBest50 = MutableStateFlow<Best50?>(null)
    val lxnsBest50: StateFlow<Best50?> = _lxnsBest50.asStateFlow()

    private val _isLoad = MutableStateFlow(false)
    val isLoad: StateFlow<Boolean> = _isLoad.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var loadCounter = 0

    fun reload(token: String) {
        apiHandler.cancelRetry()
        if (token.isEmpty()) return
        _isLoad.value = false
        _error.value = null
        loadCounter = 0
        loadRatingTrend(token)
        loadPlayerLxns(token)
        loadBest50(token)
    }

    private fun onApiComplete() {
        loadCounter++
        if (loadCounter >= 3) {
            _isLoad.value = true
        }
    }

    private fun loadRatingTrend(token: String) {
        apiHandler.call(
            apiCall = { lxnsApi.getPlayerTrend(token) },
            onSuccess = {
                _lxnsRatingTrend.value = it
                onApiComplete()
            },
            onError = {
                _error.value = it
                onApiComplete()
            }
        )
    }

    private fun loadPlayerLxns(token: String) {
        apiHandler.call(
            apiCall = { lxnsApi.getPlayerInfo(token) },
            onSuccess = {
                if (it != null) _lxnsPlayerMaiInfo.value = it
                onApiComplete()
            },
            onError = {
                _error.value = it
                onApiComplete()
            }
        )
    }

    private fun loadBest50(token: String) {
        apiHandler.call(
            apiCall = { lxnsApi.getPlayerB50(token) },
            onSuccess = {
                if (it != null) _lxnsBest50.value = it
                onApiComplete()
            },
            onError = {
                _error.value = it
                onApiComplete()
            }
        )
    }

    fun uploadScoreLxns() {
        // TODO: implement upload via lxnsApi.uploadPlayerScores()
    }
}
