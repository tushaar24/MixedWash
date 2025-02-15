package com.mixedwash.features.createOrder.di

import com.mixedwash.features.createOrder.domain.DeleteAddressUseCase
import com.mixedwash.features.createOrder.domain.FindUserByPhoneUseCase
import com.mixedwash.features.createOrder.domain.GetAddressScreenUseCase
import com.mixedwash.features.createOrder.domain.LoadSlotsWithOffersUseCase
import com.mixedwash.features.createOrder.domain.SelectAddressUseCase
import com.mixedwash.features.createOrder.domain.SelectSlotAndOffersUseCase
import com.mixedwash.features.createOrder.domain.UpsertAddressUseCase
import com.mixedwash.features.createOrder.presentation.screens.AddressScreenViewModel
import com.mixedwash.features.createOrder.presentation.screens.PhoneScreenViewModel
import com.mixedwash.features.createOrder.presentation.screens.SlotSelectionScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val createOrderModule = module {
    single { FindUserByPhoneUseCase }
    single { GetAddressScreenUseCase() }
    single { UpsertAddressUseCase() }
    single { SelectAddressUseCase() }
    single { DeleteAddressUseCase() }
    single { LoadSlotsWithOffersUseCase() }
    single { SelectSlotAndOffersUseCase() }
    viewModelOf(::AddressScreenViewModel)
    viewModelOf(::SlotSelectionScreenViewModel)
    viewModelOf(::PhoneScreenViewModel)
}

