package com.mixedwash.features.support.di

import com.mixedwash.features.support.data.FaqRepositoryImpl
import com.mixedwash.features.support.data.FaqService
import com.mixedwash.features.support.domain.FaqRepository
import com.mixedwash.features.support.presentation.FaqScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val SupportModule = module {
    single<FaqService> { FaqService(get()) }
    single<FaqRepository> { FaqRepositoryImpl() }
    viewModelOf(::FaqScreenViewModel)
}