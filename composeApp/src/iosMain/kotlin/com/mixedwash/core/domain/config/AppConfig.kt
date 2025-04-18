package com.mixedwash.core.domain.config

actual object AppConfig {
    actual val bypassLocationCheck: Boolean
        get() = false
    actual val useStagingOrdersService: Boolean
        get() = false
}