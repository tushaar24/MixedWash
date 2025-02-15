package com.mixedwash

import com.mixedwash.core.network.di.networkModule
import com.mixedwash.features.createOrder.di.createOrderModule
import com.mixedwash.features.laundryServices.di.laundryServiceModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(createOrderModule, laundryServiceModule, networkModule)
}