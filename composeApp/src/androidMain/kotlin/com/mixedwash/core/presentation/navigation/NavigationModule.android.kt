package com.mixedwash.core.presentation.navigation

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun targetNavigationModule(): Module = module {
    singleOf(::AppCloser)
}