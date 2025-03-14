package com.mixedwash

import com.mixedwash.core.data.di.DataModule
import com.mixedwash.core.di.ConfigModule
import com.mixedwash.core.di.NetworkModule
import com.mixedwash.features.common.di.CommonModule
import com.mixedwash.features.history.di.HistoryModule
import com.mixedwash.features.laundryServices.di.LaundryServiceModule
import com.mixedwash.features.support.di.SupportModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        CommonModule,
        LaundryServiceModule,
        NetworkModule,
        DataModule,
        ConfigModule,
        SupportModule,
        HistoryModule
    )
}
