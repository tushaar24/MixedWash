package com.mixedwash.core.domain.config

import com.mixedwash.BuildConfig

actual object AppConfig {
    actual val bypassLocationCheck: Boolean = BuildConfig.BYPASS_LOCATION_CHECK
    actual val useStagingOrdersService: Boolean = BuildConfig.USE_STAGING_ORDERS_SERVICE
    actual val versionString: String = BuildConfig.VERSION_NAME
}