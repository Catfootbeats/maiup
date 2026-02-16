package xyz.catfootbeats.maiup.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DataStoreFactory {
    private companion object {
        const val DATA_STORE_FILE_NAME = "maiup_preferences.preferences_pb"
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): DataStore<Preferences> {
        // 在 iOS 上，我们需要指定一个文件路径来存储 DataStore 数据。
        // 通常将其放在应用的文档目录下。
        val documentDirectory: String? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )?.path

        val producePath = {
            requireNotNull(documentDirectory) + "/${DATA_STORE_FILE_NAME}"
        }

        // 使用 PreferenceDataStoreFactory.createWithPath 创建 DataStore
        return PreferenceDataStoreFactory.createWithPath(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            produceFile = { producePath().toPath() }
        )
    }
}
