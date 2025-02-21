package com.mixedwash.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mixedwash.MixedWashApplication
import com.mixedwash.core.data.createDataStore
import com.mixedwash.core.data.util.AppCoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataModule(): Module = module {
    single<DataStore<Preferences>> {
        createDataStore(context = androidApplication())
    }

    single<AppCoroutineScope> {
        (androidApplication() as MixedWashApplication).appCoroutineScope
    }
}
