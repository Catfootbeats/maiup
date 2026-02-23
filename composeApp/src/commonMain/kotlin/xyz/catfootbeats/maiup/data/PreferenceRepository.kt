package xyz.catfootbeats.maiup.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.catfootbeats.maiup.model.Game
import xyz.catfootbeats.maiup.model.ThemeMode

data class MaiupSettings(
    // token
    val lxnsToken: String = "",
    val waterfishToken: String = "",
    // 主题相关
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isMonet: Boolean = false,
    val keyColor: Color = Color(0xFF66CCFF),
    // 模式
    val game: Game = Game.MAI
)

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        const val DEFAULT_LXNS_API = ""
        const val DEFAULT_WATERFISH_TOKEN = ""
        val DEFAULT_THEME_MODE = ThemeMode.SYSTEM
        val DEFAULT_APP_MODE = Game.CHU
    }

    private val lxnsAPIKey = stringPreferencesKey("lxns_api")
    private val waterfishTokenKey = stringPreferencesKey("waterfish_token")
    private val themeModeKey = stringPreferencesKey("theme_mode")
    private val isMonetKey = booleanPreferencesKey("is_monet")
    private val keyColorKey =  intPreferencesKey("key_color")
    private val appModeKey = stringPreferencesKey("app_mode")

    val settings: Flow<MaiupSettings> = dataStore.data.map {
        MaiupSettings(
            lxnsToken = it[lxnsAPIKey] ?: DEFAULT_LXNS_API,
            waterfishToken=it[waterfishTokenKey] ?: DEFAULT_WATERFISH_TOKEN,

            themeMode=try{
                ThemeMode.valueOf(it[themeModeKey] ?: DEFAULT_THEME_MODE.name)
            } catch (_: IllegalArgumentException) {
                DEFAULT_THEME_MODE
            },
            isMonet=it[isMonetKey] ?: false,
            keyColor= Color(it[keyColorKey]?:0xFF66CCFF.toInt()),

            game=try {
                Game.valueOf(it[appModeKey] ?: DEFAULT_APP_MODE.name)
            } catch (_: IllegalArgumentException){
                DEFAULT_APP_MODE
            }
        )
    }

    suspend fun updateAppMode(game: Game) {
        dataStore.edit{
            it[appModeKey] = game.name
        }
    }
    suspend fun updateKeyColor(keyColor: Color){
        dataStore.edit {
            it[keyColorKey] = keyColor.toArgb()
        }
    }
    suspend fun updateMonet(isMonet: Boolean){
        dataStore.edit {
            it[isMonetKey] = isMonet
        }
    }
    suspend fun updateTheme(themeMode: ThemeMode){
        dataStore.edit{
            it[themeModeKey] = themeMode.name
        }
    }
    suspend fun updateLxnsAPI(lxnsAPI: String){
        dataStore.edit {
            it[lxnsAPIKey] = lxnsAPI
        }
    }
    suspend fun updateWaterfishToken(waterfishToken: String){
        dataStore.edit {
            it[waterfishTokenKey] = waterfishToken
        }
    }
}