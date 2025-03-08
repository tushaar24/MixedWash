package com.mixedwash.core.data.di

import com.mixedwash.core.data.FirebaseUserService
import com.mixedwash.core.data.UserService
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun platformDataModule(): Module

val DataModule = module {
    includes(platformDataModule())
    single { FirebaseUserService( get() ) } bind UserService::class
}
