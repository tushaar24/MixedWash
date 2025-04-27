package com.mixedwash.core.feature.crash.data

import com.mixedwash.core.feature.crash.domain.CrashReporter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CrashReporterHolder: KoinComponent{
    val instance: CrashReporter by inject()
}