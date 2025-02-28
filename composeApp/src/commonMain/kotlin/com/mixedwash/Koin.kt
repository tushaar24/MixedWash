package com.mixedwash

import com.mixedwash.core.data.di.dataModule
import com.mixedwash.core.di.networkModule
import com.mixedwash.features.common.di.commonModule
import com.mixedwash.features.laundryServices.di.laundryServiceModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule, laundryServiceModule, networkModule, dataModule)
}
