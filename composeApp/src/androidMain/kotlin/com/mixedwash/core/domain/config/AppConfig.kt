package com.mixedwash.core.domain.config

import com.mixedwash.BuildConfig

actual class AppConfig {
    actual val bypassLocationCheck: Boolean = BuildConfig.BYPASS_LOCATION_CHECK
}