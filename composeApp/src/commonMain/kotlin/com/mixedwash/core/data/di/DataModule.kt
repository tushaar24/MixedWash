package com.mixedwash.core.data.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformDataModule(): Module

val DataModule = module {
    includes(platformDataModule())
}
