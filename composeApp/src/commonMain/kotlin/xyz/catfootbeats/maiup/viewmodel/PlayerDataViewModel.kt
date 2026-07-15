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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _hasData = MutableStateFlow(false)
    val hasData: StateFlow<Boolean> = _hasData.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var loadCounter = 0
    private var successCount = 0
    private val errors = mutableListOf<String>()

    fun reload(token: String) {
         if (_isRefreshing.value) return
         apiHandler.cancelAll()
         if (token.isEmpty()) return
         _isRefreshing.value = true
         _error.value = null
         loadCounter = 0
         successCount = 0
         errors.clear()
         loadRatingTrend(token)
         loadPlayerLxns(token)
         loadBest50(token)
    }

    private fun onApiComplete(success: Boolean, errorMsg: String? = null) {
        loadCounter++
        if (success) successCount++
        if (errorMsg != null) errors.add(errorMsg)
        if (loadCounter >= 3) {
            _isRefreshing.value = false
            _hasData.value = successCount > 0
            _error.value = errors.takeIf { it.isNotEmpty() }?.joinToString("\n")
        }
    }

    private fun loadRatingTrend(token: String) {
        apiHandler.call(
            apiCall = { lxnsApi.getPlayerTrend(token) },
            onSuccess = { _lxnsRatingTrend.value = it; onApiComplete(success = true) },
            onError = { onApiComplete(success = false, errorMsg = it) }
        )
    }

    private fun loadPlayerLxns(token: String) {
        apiHandler.call(
            apiCall = { lxnsApi.getPlayerInfo(token) },
            onSuccess = { if (it != null) _lxnsPlayerMaiInfo.value = it; onApiComplete(success = true) },
            onError = { onApiComplete(success = false, errorMsg = it) }
        )
    }

    private fun loadBest50(token: String) {
        apiHandler.call(
            apiCall = { lxnsApi.getPlayerB50(token) },
            onSuccess = { if (it != null) _lxnsBest50.value = it; onApiComplete(success = true) },
            onError = { onApiComplete(success = false, errorMsg = it) }
        )
    }

    fun uploadScoreLxns() {
        // TODO: implement upload via lxnsApi.uploadPlayerScores()
    }
}
