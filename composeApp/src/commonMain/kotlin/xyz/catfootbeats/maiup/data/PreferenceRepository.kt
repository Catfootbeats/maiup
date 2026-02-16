package xyz.catfootbeats.maiup.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode{
    LIGHT,DARK,SYSTEM
}

data class MaiupSettings(
    val lxnsAPI: String = "",
    val waterfishToken: String = "",
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        const val DEFAULT_LXNS_API = ""
        const val DEFAULT_WATERFISH_TOKEN = ""
        val DEFAULT_THEME_MODE = ThemeMode.SYSTEM.name
    }

    private val lxnsAPIKey = stringPreferencesKey("lxns_api")
    private val waterfishTokenKey = stringPreferencesKey("waterfish_token")
    private val themeModeKey = stringPreferencesKey("theme_mode")

    val settings: Flow<MaiupSettings> = dataStore.data.map {
        MaiupSettings(
            it[lxnsAPIKey] ?: DEFAULT_LXNS_API,
            it[waterfishTokenKey] ?: DEFAULT_WATERFISH_TOKEN,
            try{
                ThemeMode.valueOf(it[themeModeKey] ?: DEFAULT_THEME_MODE)
            } catch (_: IllegalArgumentException) {
                ThemeMode.SYSTEM
            }
        )
    }

    suspend fun updateTheme(
        themeMode: ThemeMode
    ){
        dataStore.edit{
            it[themeModeKey] = themeMode.name
        }
    }

    suspend fun updateLxnsAPI(
        lxnsAPI: String
    ){
        dataStore.edit {
            it[lxnsAPIKey] = lxnsAPI
        }
    }

    suspend fun updateWaterfishToken(
        waterfishToken: String
    ){
        dataStore.edit {
            it[waterfishTokenKey] = waterfishToken
        }
    }
}