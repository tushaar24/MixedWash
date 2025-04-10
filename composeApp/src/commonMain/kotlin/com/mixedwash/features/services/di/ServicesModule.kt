package com.mixedwash.features.services.di

import com.mixedwash.features.services.data.remote.MockServicesDataRepository
import com.mixedwash.features.services.data.remote.repository.ServicesRepositoryImpl
import com.mixedwash.features.services.domain.ServicesDataRepository
import com.mixedwash.features.services.domain.ServicesRepository
import com.mixedwash.features.services.presentation.ServicesScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServicesModule = module {
    single<ServicesDataRepository> {
        MockServicesDataRepository()
    } bind ServicesDataRepository::class

    single<ServicesRepository> {
        ServicesRepositoryImpl(get())
    }

    viewModelOf(::ServicesScreenViewModel)
}
