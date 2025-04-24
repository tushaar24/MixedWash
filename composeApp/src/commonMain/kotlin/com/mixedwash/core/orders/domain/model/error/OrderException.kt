package com.mixedwash.core.orders.domain.model.error

import com.mixedwash.core.presentation.util.Logger

sealed class OrderException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data object OrderNotFound : OrderException(message = "Order Not Found")
    data object BookingNotFound : OrderException(message = "Booking Not Found")
    data object FailedToCreateOrder : OrderException(message = "Failed to create order")
    data object IllegalStagingOperationException :
    OrderException(message = "This operation is only allowed in staging mode")
}

inline fun <T> Result<T>.onOrderError(
    crossinline orderNotFound: () -> Unit = { defaultLogger("Order Not Found") },
    crossinline bookingNotFound: () -> Unit = { defaultLogger("Booking Not Found")},
    crossinline failedToCreateOrder: () -> Unit = { defaultLogger("Failed to create order")},
    crossinline illegalStagingOperation: (Throwable) -> Unit = { defaultLogger("Illegal Staging Operation")},
    crossinline other: (Throwable) -> Unit = { defaultLogger("Unknown Error has occurred while placing order")}
): Result<T> {
    onFailure { error ->
        when (error) {
            is OrderException.OrderNotFound -> orderNotFound()
            is OrderException.BookingNotFound -> bookingNotFound()
            is OrderException.FailedToCreateOrder -> failedToCreateOrder()
            is OrderException.IllegalStagingOperationException -> illegalStagingOperation(error)
            else -> other(error)
        }
    }
    return this
}

fun defaultLogger(message : String = "Unknown Error has occurred" ) {
    Logger.e("OrderException", message)
}