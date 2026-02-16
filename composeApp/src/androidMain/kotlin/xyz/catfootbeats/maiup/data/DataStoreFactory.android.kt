package xyz.catfootbeats.maiup.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.context.GlobalContext

actual class DataStoreFactory {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "maiup_preferences" // DataStore 文件名
    )
    actual fun create(): DataStore<Preferences> {
        val context: Context = GlobalContext.get().get<Context>()
        return context.dataStore
    }
}