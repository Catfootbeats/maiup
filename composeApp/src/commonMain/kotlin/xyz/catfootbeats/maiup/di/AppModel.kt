package xyz.catfootbeats.maiup.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import xyz.catfootbeats.maiup.api.HttpClientFactory
import xyz.catfootbeats.maiup.api.LxnsApi
import xyz.catfootbeats.maiup.data.DataStoreFactory
import xyz.catfootbeats.maiup.data.PreferenceRepository
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.PlayerDataViewModel


val appModule = module {
    // ViewModel
    viewModel { MaiupViewModel(get()) }
    viewModel { PlayerDataViewModel(get(),get()) }

    // DataStore
    singleOf(::DataStoreFactory)
    single<DataStore<Preferences>> {
        val factory: DataStoreFactory = get()
        factory.create()
    }

    // HTTP Client
    singleOf(::HttpClientFactory)
    single<HttpClient> {
        val factory: HttpClientFactory = get()
        factory.create()
    }

    // Repository
    singleOf(::PreferenceRepository)

    // API
    single { LxnsApi(get()) }
}