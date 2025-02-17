package com.mixedwash.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mixedwash.core.data.createDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun targetDataModule(): Module = module {
    single<DataStore<Preferences>> {
        createDataStore(context = androidApplication())
    }
}
