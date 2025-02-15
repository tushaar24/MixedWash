package com.mixedwash.features.laundryServices.di

import com.mixedwash.features.laundryServices.data.repository.LaundryServicesRepositoryImpl
import com.mixedwash.features.laundryServices.data.service.remote.LaundryServicesService
import com.mixedwash.features.laundryServices.domain.repository.LaundryServiceRepository
import com.mixedwash.features.laundryServices.domain.useCases.FetchAllLaundryServicesUseCase
import com.mixedwash.features.laundryServices.domain.useCases.impl.FetchAllLaundryServicesUseCaseImpl
import org.koin.dsl.module


val laundryServiceModule = module {
    single { LaundryServicesService(get()) }
    single<LaundryServiceRepository> { LaundryServicesRepositoryImpl(get()) }
    single<FetchAllLaundryServicesUseCase> { FetchAllLaundryServicesUseCaseImpl(get()) }
}
