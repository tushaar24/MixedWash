package com.mixedwash.core.data.di

import com.mixedwash.core.data.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun targetDataModule(): Module = module {
    createDataStore()
}