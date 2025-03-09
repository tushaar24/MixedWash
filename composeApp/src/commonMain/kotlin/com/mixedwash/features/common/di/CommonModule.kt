package com.mixedwash.features.common.di

import com.mixedwash.TestApiKeyConfig
import com.mixedwash.features.common.data.repository.AddressRepositoryImpl
import com.mixedwash.features.common.data.service.local.LocationService
import com.mixedwash.features.common.domain.repository.AddressRepository
import com.mixedwash.features.common.domain.usecases.FindUserByPhoneUseCase
import com.mixedwash.features.common.domain.usecases.address.AddressUseCases
import com.mixedwash.features.common.domain.usecases.slots.LoadSlotsWithOffersUseCase
import com.mixedwash.features.common.domain.usecases.slots.SelectSlotAndOffersUseCase
import com.mixedwash.features.common.presentation.address.AddressScreenViewModel
import com.mixedwash.features.support.presentation.FaqScreenViewModel
import com.mixedwash.features.history.presentation.OrderHistoryScreenViewModel
import com.mixedwash.features.common.presentation.phone.PhoneScreenViewModel
import com.mixedwash.features.common.presentation.profile.ProfileEditScreenViewModel
import com.mixedwash.features.common.presentation.services.ServicesScreenViewModel
import com.mixedwash.features.common.presentation.slot_selection.SlotSelectionScreenViewModel
import com.mixedwash.features.history.di.HistoryModule
import com.mixedwash.features.location_availability.di.LocationAvailabilityModule
import com.mixedwash.features.support.di.SupportModule
import com.mixedwash.libs.loki.autocomplete.Autocomplete
import com.mixedwash.libs.loki.autocomplete.AutocompleteOptions
import com.mixedwash.libs.loki.autocomplete.AutocompletePlace
import com.mixedwash.libs.loki.autocomplete.googlemaps.googleMaps
import com.mixedwash.libs.loki.geocoder.Geocoder
import com.mixedwash.libs.loki.geocoder.googlemaps.googleMaps
import com.mixedwash.libs.loki.geolocation.Geolocator
import com.mixedwash.libs.loki.geolocation.MobileGeolocator
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {

    includes(CommonDataModule, LocationAvailabilityModule, SupportModule, HistoryModule)

    single { FindUserByPhoneUseCase }
    single { LoadSlotsWithOffersUseCase() }
    single { SelectSlotAndOffersUseCase() }

    single<Geolocator> { MobileGeolocator() }
    single<Geocoder> { Geocoder.googleMaps(
        apiKey = TestApiKeyConfig.googleApiKey,
        enableLogging = true
    ) }
    single<Autocomplete<AutocompletePlace>> {
        Autocomplete.googleMaps(
            apiKey = TestApiKeyConfig.googleApiKey,
            options = AutocompleteOptions(minimumQuery = 4),
            enableLogging = true,
        )
    }
    single<LocationService> { LocationService(get(), get(), get()) }
    single { AddressRepositoryImpl(get(), get()) } bind AddressRepository::class
    singleOf(::AddressUseCases)

    viewModelOf(::AddressScreenViewModel)
    viewModelOf(::SlotSelectionScreenViewModel)
    viewModelOf(::PhoneScreenViewModel)
    viewModelOf(::ProfileEditScreenViewModel)
    viewModelOf(::ServicesScreenViewModel)
    viewModelOf(::OrderHistoryScreenViewModel)
    viewModel { FaqScreenViewModel(get()) }

}

