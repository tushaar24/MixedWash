package com.mixedwash.core.booking.domain.model.error

import com.mixedwash.features.local_cart.domain.error.CartException.ItemNotFoundException

sealed class BookingException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data object BookingNotFound : BookingException(message = "No Booking Initialized")
}

inline fun <T> Result<T>.onBookingError(
    crossinline bookingNotFound: () -> Unit = {},
    crossinline other: (Throwable) -> Unit = { e -> e.printStackTrace() }
): Result<T> {
    onFailure { error ->
        when (error) {
            is BookingException.BookingNotFound -> bookingNotFound()
            else -> other(error)
        }
    }
    return this
}
