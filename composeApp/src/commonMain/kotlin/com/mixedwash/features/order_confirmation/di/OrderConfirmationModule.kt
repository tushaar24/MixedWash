package com.mixedwash.features.order_confirmation.di

import com.mixedwash.features.order_confirmation.presentation.OrderConfirmationScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val OrderConfirmationModule = module {
    viewModelOf(::OrderConfirmationScreenViewModel)
}