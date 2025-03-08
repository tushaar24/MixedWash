package com.mixedwash.core.presentation.navigation

import org.koin.core.module.Module
import org.koin.dsl.module


expect fun targetNavigationModule() : Module
val NavigationModule = module {
    includes(targetNavigationModule())
}