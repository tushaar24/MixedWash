package com.mixedwash.core.orders.di

import com.mixedwash.core.orders.data.repository.FirebaseOrdersRepositoryImpl
import com.mixedwash.core.orders.data.service.FirebaseOrderService
import com.mixedwash.core.orders.data.service.OrderDraftServiceImpl
import com.mixedwash.core.orders.data.service.OrderService
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.orders.domain.service.OrderDraftService
import org.koin.dsl.bind
import org.koin.dsl.module

val OrdersModule = module {
    single<OrderDraftService> { OrderDraftServiceImpl() } bind OrderDraftService::class
    single<OrderService> { FirebaseOrderService(appCoroutineScope = get(), userService = get()) }
    single<OrdersRepository> {
        FirebaseOrdersRepositoryImpl(orderDraftService = get(), orderService = get())
    } bind OrdersRepository::class
}