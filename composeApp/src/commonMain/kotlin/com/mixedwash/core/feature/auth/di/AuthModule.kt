package com.mixedwash.core.feature.auth.di

import com.mixedwash.core.feature.auth.data.FirebaseUserService
import com.mixedwash.core.feature.auth.domain.UserService
import org.koin.dsl.bind
import org.koin.dsl.module

val AuthModule = module {
    single { FirebaseUserService(get()) } bind UserService::class
}