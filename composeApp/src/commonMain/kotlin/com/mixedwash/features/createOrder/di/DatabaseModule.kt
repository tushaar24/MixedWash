package com.mixedwash.features.createOrder.di

import com.mixedwash.features.createOrder.data.local.service.AddressDataStore
import com.mixedwash.features.createOrder.data.local.service.AddressDatabase
import com.mixedwash.features.createOrder.data.local.service.AddressDatabaseBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect fun databaseTargetModule(): Module

fun createOrderDataModule () = module {
    includes(databaseTargetModule())
    single<AddressDatabase> { get<AddressDatabaseBuilder>().getDatabase() }
    singleOf(::AddressDataStore)
}