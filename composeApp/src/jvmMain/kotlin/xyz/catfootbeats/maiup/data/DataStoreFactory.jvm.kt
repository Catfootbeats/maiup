package xyz.catfootbeats.maiup.data

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual class DataStoreFactory {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    actual fun create(): DataStore<Preferences> {
        // 获取用户主目录
        val userHome = System.getProperty("user.home")
        // 创建应用数据目录
        val appDataDir = File(userHome, ".maiup")
        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }
        // 创建DataStore文件
        val dataStoreFile = File(appDataDir, "maiup_preferences.preferences_pb")
        
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = coroutineScope,
            produceFile = { dataStoreFile }
        )
    }
}