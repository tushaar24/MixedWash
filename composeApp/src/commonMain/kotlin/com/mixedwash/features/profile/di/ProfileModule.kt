package com.mixedwash.features.profile.di

import com.mixedwash.features.profile.presentation.ProfileScreenViewModel
import com.mixedwash.features.profile_edit.ProfileEditScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ProfileModule = module {
    viewModelOf(::ProfileEditScreenViewModel)
    viewModelOf(::ProfileScreenViewModel)
}