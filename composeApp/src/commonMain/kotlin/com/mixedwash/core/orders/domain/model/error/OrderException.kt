package com.mixedwash.core.orders.domain.model.error

sealed class OrderException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data object OrderNotFound : OrderException(message = "Order Not Found")
    data object BookingNotFound : OrderException(message = "Booking Not Found")
    data object IllegalStagingOperationException :
    OrderException(message = "This operation is only allowed in staging mode")
}

inline fun <T> Result<T>.onOrderError(
    crossinline orderNotFound: () -> Unit = {},
    crossinline bookingNotFound: () -> Unit = {},
    crossinline illegalStagingOperation: (Throwable) -> Unit = {},
    crossinline other: (Throwable) -> Unit = { e -> e.printStackTrace() }
): Result<T> {
    onFailure { error ->
        when (error) {
            is OrderException.OrderNotFound -> orderNotFound()
            is OrderException.BookingNotFound -> bookingNotFound()
            is OrderException.IllegalStagingOperationException -> illegalStagingOperation(error)
            else -> other(error)
        }
    }
    return this
}