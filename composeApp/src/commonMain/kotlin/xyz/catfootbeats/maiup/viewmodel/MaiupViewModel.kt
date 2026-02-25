package xyz.catfootbeats.maiup.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.data.MaiupSettings
import xyz.catfootbeats.maiup.data.PreferenceRepository
import xyz.catfootbeats.maiup.model.Game
import xyz.catfootbeats.maiup.model.ThemeMode

class MaiupViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    private val _settings = MutableStateFlow(MaiupSettings())
    val settings: StateFlow<MaiupSettings> = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            _settings.value = preferenceRepository.settings.first()
        }
    }

    fun updateAppMode(game: Game) {
        _settings.value = _settings.value.copy(game = game)
        viewModelScope.launch { preferenceRepository.updateAppMode(game) }
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

    fun updateLxnsAPI(lxnsAPI: String) {
        _settings.value = _settings.value.copy(lxnsToken = lxnsAPI)
        viewModelScope.launch { preferenceRepository.updateLxnsAPI(lxnsAPI) }
    }

    fun updateWaterfishToken(waterfishToken: String) {
        _settings.value = _settings.value.copy(waterfishToken = waterfishToken)
        viewModelScope.launch {
            preferenceRepository.updateWaterfishToken(waterfishToken)
        }
    }
}