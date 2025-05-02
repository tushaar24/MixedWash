package com.mixedwash.features.local_cart.domain.error

import com.mixedwash.core.feature.crash.data.CrashReporterHolder

sealed class CartException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    class ItemNotFoundException(val itemId: String) :
        CartException("Cart item with ID $itemId not found")

}

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
 * @see CartException.ItemNotFoundException
 */
inline fun <T> Result<T>.onCartException(
    crossinline itemNotFound: (CartException.ItemNotFoundException) -> Unit = {},
    crossinline other: (Throwable) -> Unit = { e -> e.printStackTrace() }
): Result<T> {
    onFailure { error ->
        when (error) {
            is CartException.ItemNotFoundException -> itemNotFound(error)
            else -> other(error)
        }
        CrashReporterHolder.instance.recordException(error)
    }
    return this
}