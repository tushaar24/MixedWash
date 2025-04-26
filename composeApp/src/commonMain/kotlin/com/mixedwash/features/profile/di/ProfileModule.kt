package com.mixedwash.features.profile.di

import com.mixedwash.core.domain.config.AppConfig
import com.mixedwash.features.profile.presentation.ProfileScreenViewModel
import com.mixedwash.features.profile_edit.ProfileEditScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ProfileModule = module {
    viewModelOf(::ProfileEditScreenViewModel)
    viewModel<ProfileScreenViewModel> {
        ProfileScreenViewModel(
            userService = get(),
            versionString = AppConfig.versionString
        )
    }
}