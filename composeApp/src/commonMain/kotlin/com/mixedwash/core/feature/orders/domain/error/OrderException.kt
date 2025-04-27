package com.mixedwash.core.feature.orders.domain.error

import com.mixedwash.core.feature.crash.data.CrashReporterHolder

sealed class OrderException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data object OrderNotFound : OrderException(message = "Order Not Found")
    data object BookingNotFound : OrderException(message = "Booking Not Found")
    data object FailedToCreateOrder : OrderException(message = "Failed to create order")
    data object IllegalStagingOperationException :
    OrderException(message = "This operation is only allowed in staging mode")
}

inline fun <T> Result<T>.onOrderError(
    crossinline orderNotFound: () -> Unit = {  },
    crossinline bookingNotFound: () -> Unit = {  },
    crossinline failedToCreateOrder: () -> Unit = { },
    crossinline illegalStagingOperation: (Throwable) -> Unit = {  },
    crossinline other: (Throwable) -> Unit = { }
): Result<T> {
    onFailure { error ->
        when (error) {
            is OrderException.OrderNotFound -> orderNotFound()
            is OrderException.BookingNotFound -> bookingNotFound()
            is OrderException.FailedToCreateOrder -> failedToCreateOrder()
            is OrderException.IllegalStagingOperationException -> illegalStagingOperation(error)
            else -> other(error)
        }
        CrashReporterHolder.instance.recordException(error)
    }
    return this
}

