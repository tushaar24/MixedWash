package com.mixedwash.features.order_details.di

import com.mixedwash.features.order_details.presentation.OrderDetailsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val OrderDetailsModule = module {
    viewModelOf(::OrderDetailsScreenViewModel)
}