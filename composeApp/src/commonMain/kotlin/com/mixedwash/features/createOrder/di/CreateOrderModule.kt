package com.mixedwash.features.createOrder.di

import com.mixedwash.TestApiKeyConfig
import com.mixedwash.features.createOrder.data.local.service.LocationServiceManager
import com.mixedwash.features.createOrder.data.repository.AddressRepositoryImpl
import com.mixedwash.features.createOrder.domain.repository.AddressRepository
import com.mixedwash.features.createOrder.domain.usecases.FindUserByPhoneUseCase
import com.mixedwash.features.createOrder.domain.usecases.address.AddressUseCases
import com.mixedwash.features.createOrder.domain.usecases.slots.LoadSlotsWithOffersUseCase
import com.mixedwash.features.createOrder.domain.usecases.slots.SelectSlotAndOffersUseCase
import com.mixedwash.features.createOrder.presentation.address.AddressScreenViewModel
import com.mixedwash.features.createOrder.presentation.phone.PhoneScreenViewModel
import com.mixedwash.features.createOrder.presentation.slot_selection.SlotSelectionScreenViewModel
import com.mixedwash.features.profile.ProfileEditScreenViewModel
import com.mixedwash.services.loki.autocomplete.Autocomplete
import com.mixedwash.services.loki.autocomplete.AutocompleteOptions
import com.mixedwash.services.loki.autocomplete.AutocompletePlace
import com.mixedwash.services.loki.autocomplete.googlemaps.googleMaps
import com.mixedwash.services.loki.geocoder.Geocoder
import com.mixedwash.services.loki.geocoder.googlemaps.googleMaps
import com.mixedwash.services.loki.geolocation.Geolocator
import com.mixedwash.services.loki.geolocation.MobileGeolocator
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


val createOrderModule = module {

includes(createOrderDataModule())

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
    single<LocationServiceManager> { LocationServiceManager(get(), get(), get()) }
    single { AddressRepositoryImpl(get(), get()) } bind AddressRepository::class
    singleOf(::AddressUseCases)

    viewModelOf(::AddressScreenViewModel)
    viewModelOf(::SlotSelectionScreenViewModel)
    viewModelOf(::PhoneScreenViewModel)
    viewModelOf(::ProfileEditScreenViewModel)

}

