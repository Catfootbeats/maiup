package xyz.catfootbeats.maiup.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreFactory() {
    fun create(): DataStore<Preferences>
}