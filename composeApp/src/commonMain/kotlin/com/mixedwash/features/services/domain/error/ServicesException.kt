package com.mixedwash.features.services.domain.error

import com.mixedwash.core.feature.crash.data.CrashReporterHolder

sealed class ServicesException : Exception() {
    data class ServiceItemNotFoundException(val id: String) : ServicesException()
    data class ServiceNotFoundException(val id: String) : ServicesException()
    data class ServicesCannotBeFetchedException(override val message: String) : ServicesException()
}

inline fun <T> Result<T>.onServiceException(
    crossinline onServiceItemNotFound: (ServicesException.ServiceItemNotFoundException) -> Unit = {},
    crossinline onServiceNotFoundException: (ServicesException.ServiceNotFoundException) -> Unit = {},
    crossinline onServicesCannotBeFetchedException: (ServicesException.ServicesCannotBeFetchedException) -> Unit = {},
    crossinline onOtherException: (Throwable) -> Unit = {}
) {
    onFailure {
        when (it) {
            is ServicesException.ServiceItemNotFoundException -> onServiceItemNotFound(it)
            is ServicesException.ServiceNotFoundException -> onServiceNotFoundException(it)
            is ServicesException.ServicesCannotBeFetchedException -> onServicesCannotBeFetchedException(it)
            else -> onOtherException(it)
        }
        CrashReporterHolder.instance.recordException(it)
    }
}