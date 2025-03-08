package com.mixedwash

import com.mixedwash.core.data.di.DataModule
import com.mixedwash.core.di.ConfigModule
import com.mixedwash.core.di.NetworkModule
import com.mixedwash.core.di.TargetConfigModule
import com.mixedwash.features.common.di.CommonModule
import com.mixedwash.features.laundryServices.di.LaundryServiceModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(CommonModule, LaundryServiceModule, NetworkModule, DataModule, ConfigModule)
}
