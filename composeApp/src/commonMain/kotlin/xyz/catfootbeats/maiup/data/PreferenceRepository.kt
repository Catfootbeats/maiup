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
import xyz.catfootbeats.maiup.data.MaiupSettings.Companion.DEFAULT_IS_MONET
import xyz.catfootbeats.maiup.data.MaiupSettings.Companion.DEFAULT_THEME_MODE
import xyz.catfootbeats.maiup.data.MaiupSettings.Companion.DEFAULT_WATERFISH_TOKEN
import xyz.catfootbeats.maiup.data.MaiupSettings.Companion.DEFAULT_KEY_COLOR
import xyz.catfootbeats.maiup.model.ThemeMode

data class MaiupSettings(
    // OAuth
    val accessToken: String = "",
    val refreshToken: String = "",
    // 主题
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isMonet: Boolean = false,
    val keyColor: Color = Color(0xFF66CCFF),
) {
    val isAuthorized: Boolean get() = accessToken.isNotEmpty()

    companion object {
        const val DEFAULT_WATERFISH_TOKEN = ""
        val DEFAULT_THEME_MODE = ThemeMode.SYSTEM
        const val DEFAULT_IS_MONET = false
        val DEFAULT_KEY_COLOR = Color(0xFF66CCFF)
    }
}

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val themeModeKey = stringPreferencesKey("theme_mode")
    private val isMonetKey = booleanPreferencesKey("is_monet")
    private val keyColorKey = intPreferencesKey("key_color")

    val settings: Flow<MaiupSettings> = dataStore.data.map {
        MaiupSettings(
            accessToken = it[accessTokenKey] ?: "",
            refreshToken = it[refreshTokenKey] ?: "",
            themeMode = try {
                ThemeMode.valueOf(it[themeModeKey] ?: DEFAULT_THEME_MODE.name)
            } catch (_: IllegalArgumentException) {
                DEFAULT_THEME_MODE
            },
            isMonet = it[isMonetKey] ?: DEFAULT_IS_MONET,
            keyColor = Color(it[keyColorKey] ?: DEFAULT_KEY_COLOR.toArgb()),
        )
    }

    suspend fun updateAccessToken(token: String) {
        dataStore.edit { it[accessTokenKey] = token }
    }

    suspend fun updateRefreshToken(token: String) {
        dataStore.edit { it[refreshTokenKey] = token }
    }

    suspend fun updateKeyColor(keyColor: Color) {
        dataStore.edit { it[keyColorKey] = keyColor.toArgb() }
    }

    suspend fun updateMonet(isMonet: Boolean) {
        dataStore.edit { it[isMonetKey] = isMonet }
    }

    suspend fun updateTheme(themeMode: ThemeMode) {
        dataStore.edit { it[themeModeKey] = themeMode.name }
    }
}
