package com.mixedwash.features.local_cart.di

import com.mixedwash.features.local_cart.data.database.CartDao
import com.mixedwash.features.local_cart.data.database.CartDatabase
import com.mixedwash.features.local_cart.data.database.CartDatabaseBuilder
import com.mixedwash.features.local_cart.data.repository.LocalCartRepositoryImpl
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun TargetLocalCartModule(): Module

val LocalCartModule = module {
    includes(TargetLocalCartModule())
    single<CartDatabase> { get<CartDatabaseBuilder>().getDatabase() }
    single<CartDao> {get<CartDatabase>().dao()}
    single<LocalCartRepository> { LocalCartRepositoryImpl(get())} bind LocalCartRepository::class
}