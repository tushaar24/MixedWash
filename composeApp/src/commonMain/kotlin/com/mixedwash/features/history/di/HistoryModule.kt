package com.mixedwash.features.history.di

import com.mixedwash.features.history.data.HistoryRepositoryImpl
import com.mixedwash.features.history.data.HistoryService
import com.mixedwash.features.history.domain.HistoryRepository
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import com.mixedwash.features.history.presentation.OrderHistoryScreenViewModel

val HistoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl() }
    single<HistoryService> { HistoryService(get()) }
    viewModelOf(::OrderHistoryScreenViewModel)
}