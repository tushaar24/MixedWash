package com.mixedwash.features.common.di

import com.mixedwash.TestApiKeyConfig
import com.mixedwash.core.booking.di.BookingsModule
import com.mixedwash.core.presentation.navigation.NavigationModule
import com.mixedwash.features.address.di.AddressModule
import com.mixedwash.features.booking_details.di.BookingDetailsModule
import com.mixedwash.features.common.data.service.LocationService
import com.mixedwash.features.common.domain.usecases.FindUserByPhoneUseCase
import com.mixedwash.features.common.presentation.phone.PhoneScreenViewModel
import com.mixedwash.features.getting_started.OnboardingScreenViewModel
import com.mixedwash.features.home.di.HomeModule
import com.mixedwash.features.local_cart.di.LocalCartModule
import com.mixedwash.features.location_availability.di.LocationAvailabilityModule
import com.mixedwash.features.services.di.ServicesModule
import com.mixedwash.features.slot_selection.di.SlotSelectionModule
import com.mixedwash.libs.loki.autocomplete.Autocomplete
import com.mixedwash.libs.loki.autocomplete.AutocompleteOptions
import com.mixedwash.libs.loki.autocomplete.AutocompletePlace
import com.mixedwash.libs.loki.autocomplete.googlemaps.googleMaps
import com.mixedwash.libs.loki.geocoder.Geocoder
import com.mixedwash.libs.loki.geocoder.googlemaps.googleMaps
import com.mixedwash.libs.loki.geolocation.Geolocator
import com.mixedwash.libs.loki.geolocation.MobileGeolocator
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val CommonModule = module {

    includes(
        AddressModule,
        LocationAvailabilityModule,
        NavigationModule,
        HomeModule,
        ServicesModule,
        LocalCartModule,
        BookingsModule,
        SlotSelectionModule,
        BookingDetailsModule
    )

    single { FindUserByPhoneUseCase }

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
    single<LocationService> {
        LocationService(
            geolocator = get(),
            geocoder = get(),
            autocomplete = get()
        )
    }
    viewModelOf(::PhoneScreenViewModel)
    viewModelOf(::OnboardingScreenViewModel)

}

