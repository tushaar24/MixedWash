package com.mixedwash.core.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun TargetConfigModule() : Module

val ConfigModule = module {
    includes(TargetConfigModule())
}