package com.mixedwash.features.home.di

import com.mixedwash.features.home.data.MockHomeScreenDataRepositoryImpl
import com.mixedwash.features.home.domain.HomeScreenDataRepository
import com.mixedwash.features.home.presentation.HomeScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val HomeModule = module {
    single{ MockHomeScreenDataRepositoryImpl() } bind HomeScreenDataRepository::class
    viewModelOf(::HomeScreenViewModel)
}

