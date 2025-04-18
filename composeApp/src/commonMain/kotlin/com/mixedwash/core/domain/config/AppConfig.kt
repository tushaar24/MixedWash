package com.mixedwash.core.domain.config

expect object AppConfig {
    val bypassLocationCheck: Boolean
    val useStagingOrdersService: Boolean
}