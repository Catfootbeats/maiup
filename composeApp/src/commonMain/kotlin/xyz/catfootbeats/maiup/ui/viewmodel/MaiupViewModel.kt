package xyz.catfootbeats.maiup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.data.ThemeMode
import xyz.catfootbeats.maiup.data.MaiupSettings
import xyz.catfootbeats.maiup.data.PreferenceRepository

class MaiupViewModel(
    private val preferenceRepository: PreferenceRepository
): ViewModel(){
    val settingsState = MutableStateFlow( MaiupSettings() )
    init {
        viewModelScope.launch {
            settingsState.value = preferenceRepository.settings.first()
        }
    }

    fun updateTheme(themeMode: ThemeMode){
        settingsState.value = settingsState.value.copy(themeMode=themeMode)
        viewModelScope.launch { preferenceRepository.updateTheme(themeMode) } }
    fun updateLxnsAPI(lxnsAPI: String){
        settingsState.value = settingsState.value.copy(lxnsAPI=lxnsAPI)
        viewModelScope.launch { preferenceRepository.updateLxnsAPI(lxnsAPI) }}
    fun updateWaterfishToken(waterfishToken: String){
        settingsState.value = settingsState.value.copy(waterfishToken = waterfishToken)
        viewModelScope.launch {
            preferenceRepository.updateWaterfishToken(waterfishToken) }}
}