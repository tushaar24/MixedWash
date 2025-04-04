package com.mixedwash.features.slot_selection.domain.model.error

sealed class OrderDraftCreationException(message: String, cause: Throwable? = null) :
    Exception(message, cause) {
    data object AddressNotFoundException : OrderDraftCreationException(message = "No address selected")
    data object AddressNotServiceableException :
        OrderDraftCreationException(message = "Selected address is not serviceable")
    data object EmptyCartException : OrderDraftCreationException(message = "Cart is empty")
    data object NoSlotsSelectedException : OrderDraftCreationException(message = "No slots selected")
    data object InvalidSlotsException : OrderDraftCreationException(message = "Invalid slots selected")
}

inline fun <T> Result<T>.onErrorOrderDraftCreation(
    crossinline addressNotFound: () -> Unit = {},
    crossinline addressNotServiceable: () -> Unit = {},
    crossinline emptyCart: () -> Unit = {},
    crossinline noSlotsSelected: () -> Unit = {},
    crossinline invalidSlots: () -> Unit = {},
    crossinline other: (Throwable) -> Unit = { e -> e.printStackTrace() }
): Result<T> {
    onFailure { error ->
        when (error) {
            is OrderDraftCreationException.AddressNotFoundException -> addressNotFound()
            is OrderDraftCreationException.AddressNotServiceableException -> addressNotServiceable()
            is OrderDraftCreationException.EmptyCartException -> emptyCart()
            is OrderDraftCreationException.NoSlotsSelectedException -> noSlotsSelected()
            is OrderDraftCreationException.InvalidSlotsException -> invalidSlots()
            else -> other(error)
        }
    }
    return this
}