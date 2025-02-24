package com.mixedwash.features.common.di

import com.mixedwash.features.common.data.service.local.AddressDataStore
import com.mixedwash.features.common.data.service.local.AddressDatabase
import com.mixedwash.features.common.data.service.local.AddressDatabaseBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect fun databaseTargetModule(): Module

fun commonDataModule () = module {
    includes(databaseTargetModule())
    single<AddressDatabase> { get<AddressDatabaseBuilder>().getDatabase() }
    singleOf(::AddressDataStore)
}