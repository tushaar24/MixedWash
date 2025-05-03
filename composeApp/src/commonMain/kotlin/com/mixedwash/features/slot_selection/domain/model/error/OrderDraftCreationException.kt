package com.mixedwash.features.slot_selection.domain.model.error

import com.mixedwash.core.feature.crash.data.CrashReporterHolder

sealed class OrderDraftCreationException(message: String, cause: Throwable? = null) :
    Exception(message, cause) {
    data object AddressNotFoundException : OrderDraftCreationException(message = "No address selected")
    data class AddressNotServiceableException(override val message: String?, override val cause: Throwable? = null) :
        OrderDraftCreationException(message = message?: "Selected address is not serviceable", cause = cause)
    data object EmptyCartException : OrderDraftCreationException(message = "Cart is empty")
    data object NoSlotsSelectedException : OrderDraftCreationException(message = "No slots selected")
    data class InvalidSlotsException(override val message: String?=null) : OrderDraftCreationException(message = message?: "Invalid slots exception")
}

inline fun <T> Result<T>.onErrorOrderDraftCreation(
    crossinline addressNotFound: (Throwable) -> Unit = {},
    crossinline addressNotServiceable: (Throwable) -> Unit = {},
    crossinline emptyCart: (Throwable) -> Unit = {},
    crossinline noSlotsSelected: (Throwable) -> Unit = {},
    crossinline invalidSlots: (Throwable) -> Unit = {},
    crossinline other: (Throwable) -> Unit = { e -> e.printStackTrace() }
): Result<T> {
    onFailure { error ->
        when (error) {
            is OrderDraftCreationException.AddressNotFoundException -> addressNotFound(error)
            is OrderDraftCreationException.AddressNotServiceableException -> addressNotServiceable(error)
            is OrderDraftCreationException.EmptyCartException -> emptyCart(error)
            is OrderDraftCreationException.NoSlotsSelectedException -> noSlotsSelected(error)
            is OrderDraftCreationException.InvalidSlotsException -> invalidSlots(error)
            else -> other(error)
        }
        CrashReporterHolder.instance.log(error.message ?: "Unknown error")
    }
    return this
}