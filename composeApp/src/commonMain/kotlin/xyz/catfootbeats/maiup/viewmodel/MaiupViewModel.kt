package xyz.catfootbeats.maiup.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.api.LxnsApi
import xyz.catfootbeats.maiup.data.MaiupSettings
import xyz.catfootbeats.maiup.data.PreferenceRepository
import xyz.catfootbeats.maiup.model.ThemeMode
import xyz.catfootbeats.maiup.utils.OAuthConstants
import xyz.catfootbeats.maiup.utils.computeCodeChallenge
import xyz.catfootbeats.maiup.utils.generateCodeVerifier
import xyz.catfootbeats.maiup.utils.openUrl
import xyz.catfootbeats.maiup.utils.waitForOAuthCallback

class MaiupViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val lxnsApi: LxnsApi,
) : ViewModel() {
    private val _settings = MutableStateFlow(MaiupSettings())
    val settings: StateFlow<MaiupSettings> = _settings.asStateFlow()

    private val _isSettingsLoaded = MutableStateFlow(false)
    val isSettingsLoaded: StateFlow<Boolean> = _isSettingsLoaded.asStateFlow()

    private val _isAuthorizing = MutableStateFlow(false)
    val isAuthorizing: StateFlow<Boolean> = _isAuthorizing.asStateFlow()

    private val _oauthError = MutableStateFlow<String?>(null)
    val oauthError: StateFlow<String?> = _oauthError.asStateFlow()

    init {
        viewModelScope.launch {
            _settings.value = preferenceRepository.settings.first()
            _isSettingsLoaded.value = true
        }
    }

    // ---- PKCE OAuth 授权 ----

    fun authorizeOAuth() {
        if (_isAuthorizing.value) return
        _isAuthorizing.value = true
        _oauthError.value = null

        viewModelScope.launch {
            try {
                val codeVerifier = generateCodeVerifier()
                val codeChallenge = computeCodeChallenge(codeVerifier)

                openUrl(OAuthConstants.buildAuthorizeUrl(codeChallenge))
                val code = waitForOAuthCallback(OAuthConstants.CALLBACK_PORT)

                val response = lxnsApi.exchangeToken(
                    clientId = OAuthConstants.CLIENT_ID,
                    code = code,
                    redirectUri = OAuthConstants.REDIRECT_URI,
                    codeVerifier = codeVerifier
                )
                if (response.success && response.data != null) {
                    updateAccessToken(response.data.access_token)
                    updateRefreshToken(response.data.refresh_token)
                } else {
                    _oauthError.value = "OAuth 授权失败 (${response.code})"
                }
            } catch (e: Exception) {
                _oauthError.value = e.message ?: "OAuth 授权失败"
            } finally {
                _isAuthorizing.value = false
            }
        }
    }

     suspend fun tryRefreshToken(): String {
         val currentRefresh = _settings.value.refreshToken
         if (currentRefresh.isEmpty()) return _settings.value.accessToken
         return try {
             val response = lxnsApi.refreshToken(
                 clientId = OAuthConstants.CLIENT_ID,
                 refreshToken = currentRefresh
             )
             if (response.success && response.data != null) {
                 updateAccessToken(response.data.access_token)
                 updateRefreshToken(response.data.refresh_token)
                 response.data.access_token
             } else _settings.value.accessToken
         } catch (_: Exception) { _settings.value.accessToken }
     }

    fun clearOAuth() {
        viewModelScope.launch {
            preferenceRepository.updateAccessToken("")
            preferenceRepository.updateRefreshToken("")
            _settings.value = _settings.value.copy(accessToken = "", refreshToken = "")
        }
    }

    // ---- 设置更新 ----

    fun updateAccessToken(token: String) {
        _settings.value = _settings.value.copy(accessToken = token)
        viewModelScope.launch { preferenceRepository.updateAccessToken(token) }
    }

    fun updateRefreshToken(token: String) {
        _settings.value = _settings.value.copy(refreshToken = token)
        viewModelScope.launch { preferenceRepository.updateRefreshToken(token) }
    }

    fun updateKeyColor(keyColor: Color) {
        _settings.value = _settings.value.copy(keyColor = keyColor)
        viewModelScope.launch { preferenceRepository.updateKeyColor(keyColor) }
    }

    fun updateMonet(isMonet: Boolean) {
        _settings.value = _settings.value.copy(isMonet = isMonet)
        viewModelScope.launch { preferenceRepository.updateMonet(isMonet) }
    }

    fun updateTheme(themeMode: ThemeMode) {
        _settings.value = _settings.value.copy(themeMode = themeMode)
        viewModelScope.launch { preferenceRepository.updateTheme(themeMode) }
    }
}
