package com.mixedwash

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object AddressScreen : Route()
    @Serializable
    data object PhoneScreen : Route()
    @Serializable
    data object SlotSelectionScreen : Route()
    @Serializable
    data object OrderReviewScreen : Route()
    @Serializable
    data object OrderConfirmationScreen : Route()
    @Serializable
    data object ProfileScreen : Route()
}