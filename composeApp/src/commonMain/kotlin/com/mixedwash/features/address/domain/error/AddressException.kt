package com.mixedwash.features.address.domain.error

import com.mixedwash.core.crash.data.CrashReporterHolder

class AddressNotFoundException(message: String = "Address not found") : Exception(message)
class OperationFailedException(message: String, cause: Throwable? = null) : Exception(message, cause)

fun <T> Result<T>.onAddressException(
    onAddressNotFound: (AddressNotFoundException) -> Unit = {},
    onOperationFailed: (OperationFailedException) -> Unit = {},
    other: (Throwable) -> Unit = {}
) :Result<T> {
    onFailure { error ->
        when (error) {
            is AddressNotFoundException -> onAddressNotFound(error)
            is OperationFailedException -> onOperationFailed(error)
            else -> other(error)
        }
        CrashReporterHolder.instance.recordException(error)
    }
    return this
}