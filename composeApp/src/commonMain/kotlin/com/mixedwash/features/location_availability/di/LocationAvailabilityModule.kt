package com.mixedwash.features.location_availability.di

import com.mixedwash.features.location_availability.data.LocationAvailabilityRepositoryImpl
import com.mixedwash.features.location_availability.data.MockLocationAvailabilityService
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import com.mixedwash.features.location_availability.domain.LocationAvailabilityService
import org.koin.dsl.bind
import org.koin.dsl.module

val LocationAvailabilityModule = module {
//    single<LocationAvailabilityService> { FirebaseLocationAvailabilityService() } bind LocationAvailabilityService::class
    single<LocationAvailabilityService> { MockLocationAvailabilityService() } bind LocationAvailabilityService::class
    single<LocationAvailabilityRepository> { LocationAvailabilityRepositoryImpl(get(), get()) } bind LocationAvailabilityRepository::class
}
