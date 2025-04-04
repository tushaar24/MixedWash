package com.mixedwash.features.address.domain.error

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

    }
    return this
}