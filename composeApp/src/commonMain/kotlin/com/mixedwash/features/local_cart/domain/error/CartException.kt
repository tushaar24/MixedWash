package com.mixedwash.features.local_cart.domain.error

sealed class CartException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    class ItemNotFoundException(val itemId: String) :
        CartException("Cart item with ID $itemId not found")

}