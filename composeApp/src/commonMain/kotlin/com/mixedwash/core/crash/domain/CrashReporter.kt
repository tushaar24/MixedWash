package com.mixedwash.core.crash.domain

interface CrashReporter {
    fun log(message: String)
    fun recordException(throwable: Throwable, message: String)
    fun recordException(throwable: Throwable, keys: Map<String, Any>?=null)
}