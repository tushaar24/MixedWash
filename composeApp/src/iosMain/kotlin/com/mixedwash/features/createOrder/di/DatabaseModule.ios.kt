package com.mixedwash.features.createOrder.di

import com.mixedwash.features.createOrder.data.local.service.AddressDatabaseBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun databaseTargetModule(): Module = module {
    singleOf(::AddressDatabaseBuilder)
}