package com.mixedwash.features.support.di

import com.mixedwash.features.support.data.FaqRepositoryImpl
import com.mixedwash.features.support.data.FaqService
import com.mixedwash.features.support.domain.FaqRepository
import org.koin.dsl.module

val SupportModule = module {
    single<FaqService> { FaqService() }
    single<FaqRepository> { FaqRepositoryImpl(get()) }
}