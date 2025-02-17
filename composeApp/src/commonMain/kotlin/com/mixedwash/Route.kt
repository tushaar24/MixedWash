package com.mixedwash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data class AddressRoute(
        val title: String,
        val screenType: ScreenType,
        val submitText: String? = null
    ) : Route() {
        @Serializable
        enum class ScreenType {
            @SerialName("Edit")
            Edit,

            @SerialName("SelectAddress")
            SelectAddress
        }

        init {
            if (screenType == ScreenType.SelectAddress) {
                requireNotNull(submitText,
                    { "Submit text cannot be null for screen type SelectAddress" })
            }
        }
    }
    @Serializable
    data object PhoneRoute : Route()
    @Serializable
    data object SlotSelectionRoute : Route()
    @Serializable
    data object OrderReviewRoute : Route()
    @Serializable
    data object OrderConfirmationRoute : Route()

    @Serializable
    data object ProfileRoute : Route()
}

