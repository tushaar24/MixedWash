package com.mixedwash.features.common.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun targetDialerModule(): Module

val DialerModule = module {
    includes(targetDialerModule())
}
