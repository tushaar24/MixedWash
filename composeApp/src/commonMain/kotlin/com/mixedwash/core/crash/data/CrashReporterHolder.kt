package com.mixedwash.core.crash.data

import com.mixedwash.core.crash.domain.CrashReporter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CrashReporterHolder: KoinComponent{
    val instance: CrashReporter by inject()
}