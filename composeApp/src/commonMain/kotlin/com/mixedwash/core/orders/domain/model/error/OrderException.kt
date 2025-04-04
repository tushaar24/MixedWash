package com.mixedwash.core.orders.domain.model.error

sealed class OrderException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data object OrderNotFound : OrderException(message = "No Order Initialized")
}

inline fun <T> Result<T>.onOrderError(
    crossinline orderNotFound: () -> Unit = {},
    crossinline other: (Throwable) -> Unit = { e -> e.printStackTrace() }
): Result<T> {
    onFailure { error ->
        when (error) {
            is OrderException.OrderNotFound -> orderNotFound()
            else -> other(error)
        }
    }
    return this
}
