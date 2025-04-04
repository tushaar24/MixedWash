package com.mixedwash.core.orders.di

import com.mixedwash.core.orders.data.repository.MockOrdersRepositoryImpl
import com.mixedwash.core.orders.data.service.OrderDraftServiceImpl
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.orders.domain.service.OrderDraftService
import org.koin.dsl.bind
import org.koin.dsl.module

val OrdersModule = module {
    single<OrderDraftService> { OrderDraftServiceImpl() } bind OrderDraftService::class
    single<OrdersRepository> { MockOrdersRepositoryImpl(get()) } bind OrdersRepository::class
}