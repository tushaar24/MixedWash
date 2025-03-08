package com.mixedwash.core.di

import com.mixedwash.core.domain.config.AppConfig
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun TargetConfigModule(): Module = module {
    single<AppConfig> { AppConfig() }
}