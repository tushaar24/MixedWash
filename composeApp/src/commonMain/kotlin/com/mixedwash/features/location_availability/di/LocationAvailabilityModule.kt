package com.mixedwash.features.location_availability.di

import com.mixedwash.features.location_availability.data.FirebaseLocationAvailabilityService
import com.mixedwash.features.location_availability.data.LocationAvailabilityRepositoryImpl
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val LocationAvailabilityModule = module {
    single<FirebaseLocationAvailabilityService> { FirebaseLocationAvailabilityService() }
    single<LocationAvailabilityRepository> { LocationAvailabilityRepositoryImpl(get()) } bind LocationAvailabilityRepository::class
}