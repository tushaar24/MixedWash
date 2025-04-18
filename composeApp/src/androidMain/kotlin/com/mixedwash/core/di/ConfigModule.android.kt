package com.mixedwash.core.di

import com.mixedwash.core.domain.config.VersionString
import org.koin.core.module.Module
import org.koin.dsl.module
import com.mixedwash.core.domain.config.AppConfig


actual fun TargetConfigModule(): Module = module {
    single<AppConfig> { AppConfig }
    single<VersionString> { VersionString() }
}