package com.mixedwash.features.local_cart.domain.error

import com.mixedwash.features.local_cart.domain.error.CartException.ItemNotFoundException

/**
 * Handles errors that may occur during cart operations represented by a `Result`.
 *
 * This extension function provides specific error handling for `ItemNotFoundException` and a general handler for other `Throwable`s.
 *
 * @param itemNotFound A lambda executed when an `ItemNotFoundException` is encountered. Receives the exception. Defaults to no-op.
 * @param other A lambda executed for any other `Throwable`. Receives the exception. Defaults to printing the stack trace.
 * @return The original `Result`, allowing for chained operations.
 *
 * @see Result
 * @see ItemNotFoundException
 */
inline fun <T> Result<T>.onCartError(
    crossinline itemNotFound: (ItemNotFoundException) -> Unit = {},
    crossinline other: (Throwable) -> Unit = { e -> e.printStackTrace() }
): Result<T> {
    onFailure { error ->
        when (error) {
            is ItemNotFoundException -> itemNotFound(error)
            else -> other(error)
        }
    }
    return this
}
