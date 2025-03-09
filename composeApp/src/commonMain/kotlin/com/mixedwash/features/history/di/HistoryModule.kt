package com.mixedwash.features.history.di

import com.mixedwash.features.history.data.HistoryRepositoryImpl
import com.mixedwash.features.history.data.HistoryService
import com.mixedwash.features.history.domain.HistoryRepository
import org.koin.dsl.module

val HistoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl() }
    single<HistoryService> { HistoryService(get()) }
}