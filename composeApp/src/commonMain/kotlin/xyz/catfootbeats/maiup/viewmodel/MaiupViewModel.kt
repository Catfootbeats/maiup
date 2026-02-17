package xyz.catfootbeats.maiup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.data.AppMode
import xyz.catfootbeats.maiup.data.MaiupSettings
import xyz.catfootbeats.maiup.data.PreferenceRepository
import xyz.catfootbeats.maiup.data.ThemeMode

class MaiupViewModel(
    private val preferenceRepository: PreferenceRepository
): ViewModel(){
    private val _settingsState = MutableStateFlow( MaiupSettings() )
    val settingsState: StateFlow<MaiupSettings> = _settingsState.asStateFlow()
    init {
        viewModelScope.launch {
             _settingsState.value = preferenceRepository.settings.first()
        }
    }

    fun updateAppMode(appMode: AppMode){
        _settingsState.value = _settingsState.value.copy(appMode=appMode)
        viewModelScope.launch { preferenceRepository.updateAppMode(appMode) }
    }
    fun updateTheme(themeMode: ThemeMode){
        _settingsState.value = _settingsState.value.copy(themeMode=themeMode)
        viewModelScope.launch { preferenceRepository.updateTheme(themeMode) } }
    fun updateLxnsAPI(lxnsAPI: String){
        _settingsState.value = _settingsState.value.copy(lxnsToken=lxnsAPI)
        viewModelScope.launch { preferenceRepository.updateLxnsAPI(lxnsAPI) }}
    fun updateWaterfishToken(waterfishToken: String){
        _settingsState.value = _settingsState.value.copy(waterfishToken = waterfishToken)
        viewModelScope.launch {
            preferenceRepository.updateWaterfishToken(waterfishToken) }}
}