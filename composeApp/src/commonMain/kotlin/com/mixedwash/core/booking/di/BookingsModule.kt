package com.mixedwash.core.booking.di

import com.mixedwash.core.booking.data.repository.MockBookingsRepositoryImpl
import com.mixedwash.core.booking.domain.repository.BookingsRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val BookingsModule = module {
    single<BookingsRepository> { MockBookingsRepositoryImpl() } bind BookingsRepository::class
}