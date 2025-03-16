package com.mixedwash.features.slot_selection.di

import com.mixedwash.features.slot_selection.data.api.FillerSlotsApiImpl
import com.mixedwash.features.slot_selection.data.api.MockSlotsApiImpl
import com.mixedwash.features.slot_selection.data.repository.SlotsRepositoryImpl
import com.mixedwash.features.slot_selection.domain.api.FillerSlotsApi
import com.mixedwash.features.slot_selection.domain.api.SlotsApi
import com.mixedwash.features.slot_selection.domain.repository.SlotsRepository
import com.mixedwash.features.slot_selection.presentation.SlotSelectionScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val SlotSelectionModule = module {
    viewModelOf(::SlotSelectionScreenViewModel)
    single<SlotsRepository> { SlotsRepositoryImpl(get(), get()) } bind SlotsRepository::class
    single<FillerSlotsApi> { FillerSlotsApiImpl() } bind FillerSlotsApi::class
    single<SlotsApi> { MockSlotsApiImpl(get()) } bind SlotsApi::class

}