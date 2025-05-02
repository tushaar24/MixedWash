package com.mixedwash.features.common.di

import com.mixedwash.features.common.util.OpenDialer
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun targetDialerModule(): Module = module {
    factory { OpenDialer(get()) }
}