package com.mixedwash.features.common.di

import com.mixedwash.features.common.data.service.local.AddressDatabaseBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun databaseTargetModule() : Module = module {
    singleOf(::AddressDatabaseBuilder)
}