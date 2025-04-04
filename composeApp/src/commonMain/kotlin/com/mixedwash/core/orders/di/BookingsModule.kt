package com.mixedwash.core.orders.di

import com.mixedwash.core.orders.data.repository.MockOrdersRepositoryImpl
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val BookingsModule = module {
    single<OrdersRepository> { MockOrdersRepositoryImpl() } bind OrdersRepository::class
}