package xyz.catfootbeats.maiup.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import xyz.catfootbeats.maiup.data.DataStoreFactory
import xyz.catfootbeats.maiup.data.PreferenceRepository
import xyz.catfootbeats.maiup.ui.viewmodel.MaiupViewModel


val appModule = module {
    viewModelOf(::MaiupViewModel)
    singleOf(::DataStoreFactory)
    single<DataStore<Preferences>> {
        val factory: DataStoreFactory = get()
        factory.create()
    }
    singleOf(::PreferenceRepository) // Koin 隐式依赖注入参数
    viewModel { MaiupViewModel(get()) }
}