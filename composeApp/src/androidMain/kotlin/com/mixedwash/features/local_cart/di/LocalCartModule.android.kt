package com.mixedwash.features.local_cart.di

import com.mixedwash.features.local_cart.data.database.CartDatabaseBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun TargetLocalCartModule(): Module  = module{
    singleOf(::CartDatabaseBuilder)
}