package com.mixedwash.core.feature.crash.di

import com.mixedwash.core.feature.auth.domain.UserService
import com.mixedwash.core.feature.crash.data.FirebaseCrashReporter
import com.mixedwash.core.feature.crash.domain.CrashReporter
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.flow.map
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val CrashlyticsModule: Module = module {

    single {
        FirebaseCrashReporter(
            crashlytics = Firebase.crashlytics,
            userIdentifier = get<UserService>().userStateFlow.map {
                it?.uid ?: "null. user service has no user"
            },
            appConfig = get(),
            appCoroutineScope = get()
        )
    } bind CrashReporter::class
}