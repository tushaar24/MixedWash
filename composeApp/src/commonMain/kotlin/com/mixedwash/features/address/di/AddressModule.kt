package com.mixedwash.features.address.di

import com.mixedwash.features.address.data.repository.FirebaseAddressRepositoryImpl
import com.mixedwash.features.address.data.service.FirebaseAddressService
import com.mixedwash.features.address.domain.repository.AddressRepository
import com.mixedwash.features.address.presentation.AddressScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val AddressModule = module {
    single { FirebaseAddressService(get()) }
    single { FirebaseAddressRepositoryImpl(get()) } bind AddressRepository::class
    viewModelOf(::AddressScreenViewModel)
}