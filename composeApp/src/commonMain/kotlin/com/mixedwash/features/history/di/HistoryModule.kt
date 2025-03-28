package com.mixedwash.features.history.di

import com.mixedwash.features.history.presentation.OrderHistoryScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val HistoryModule = module {
    viewModelOf(::OrderHistoryScreenViewModel)
}