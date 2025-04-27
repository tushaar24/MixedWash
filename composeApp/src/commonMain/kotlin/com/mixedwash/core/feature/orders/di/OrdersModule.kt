package com.mixedwash.core.feature.orders.di

import com.mixedwash.core.domain.config.AppConfig
import com.mixedwash.core.feature.orders.data.repository.FirebaseOrdersRepositoryImpl
import com.mixedwash.core.feature.orders.data.service.OrderDraftServiceImpl
import com.mixedwash.core.feature.orders.domain.repository.OrdersRepository
import com.mixedwash.core.feature.orders.domain.service.OrderDraftService
import org.koin.dsl.bind
import org.koin.dsl.module

val OrdersModule = module {
    single<OrderDraftService> { OrderDraftServiceImpl() } bind OrderDraftService::class
    single<com.mixedwash.core.feature.orders.data.service.OrderService> {
        com.mixedwash.core.feature.orders.data.service.FirebaseOrderService(
            userService = get(),
            useStagingCollection = AppConfig.useStagingOrdersService
        )
    }
    single<OrdersRepository> {
        FirebaseOrdersRepositoryImpl(orderDraftService = get(), orderService = get())
    } bind OrdersRepository::class
}