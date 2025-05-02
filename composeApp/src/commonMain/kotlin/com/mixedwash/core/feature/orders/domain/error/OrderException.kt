package com.mixedwash.core.feature.orders.domain.error

import com.mixedwash.core.feature.crash.data.CrashReporterHolder

sealed class OrderException(cause: Throwable? = null, message: String? = cause?.message?: "OrderException") : Exception(message, cause) {
    data object OrderNotFound : OrderException(message = "Order Not Found")
    data object BookingNotFound : OrderException(message = "Booking Not Found")
    data class FailedToCreateOrder(val error: Throwable = Exception("Failed to create order")) : OrderException( error)
    data object OrderDraftNotFound: OrderException(message = "Order draft not found")
    data object IllegalStagingOperationException :
    OrderException(message = "This operation is only allowed in staging mode")
}

inline fun <T> Result<T>.onOrderError(
    crossinline orderNotFound: (Throwable) -> Unit = {  },
    crossinline bookingNotFound: (Throwable) -> Unit = {  },
    crossinline failedToCreateOrder: (Throwable) -> Unit = { },
    crossinline illegalStagingOperation: (Throwable) -> Unit = {  },
    crossinline orderDraftNotFound: (Throwable) -> Unit = {  },
    crossinline other: (Throwable) -> Unit = { }
): Result<T> {
    onFailure { error ->
        when (error) {
            is OrderException.OrderNotFound -> orderNotFound(error)
            is OrderException.BookingNotFound -> bookingNotFound(error)
            is OrderException.FailedToCreateOrder -> failedToCreateOrder(error)
            is OrderException.IllegalStagingOperationException -> illegalStagingOperation(error)
            is OrderException.OrderDraftNotFound -> orderDraftNotFound(error)
            else -> other(error)
        }
        CrashReporterHolder.instance.recordException(error)
    }
    return this
}

