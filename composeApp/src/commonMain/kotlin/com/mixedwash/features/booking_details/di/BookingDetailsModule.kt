package com.mixedwash.features.booking_details.di

import com.mixedwash.features.booking_details.presentation.BookingDetailsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val BookingDetailsModule = module {
    viewModelOf(::BookingDetailsScreenViewModel)
}