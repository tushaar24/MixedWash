package com.mixedwash.features.profile.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.mixedwash.features.profile.ProfileEditScreenViewModel
import com.mixedwash.features.profile.ProfileScreenViewModel

val ProfileModule = module {
    viewModelOf(::ProfileEditScreenViewModel)
    viewModelOf(::ProfileScreenViewModel)
}