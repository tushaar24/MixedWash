package com.mixedwash.core.domain.config

import com.mixedwash.BuildConfig

actual class VersionString {
    actual val version: String
        get() = BuildConfig.VERSION_NAME
}