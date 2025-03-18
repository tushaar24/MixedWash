package com.mixedwash

import com.mixedwash.core.presentation.util.Logger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val TAG = "ROUTE"

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
                Logger.e(TAG, "ERROR: Submit text cannot be null for screen type SelectAddress")
            }
        }
    }

    @Serializable
    data object SlotSelectionRoute : Route()

    @Serializable
    data class BookingDetailsRoute(
        val bookingId: String? = null,
        val destinationType: DestinationType
    ) : Route() {
        @Serializable
        enum class DestinationType {
            @SerialName("confirm_draft_booking")
            CONFIRM_DRAFT_BOOKING,

            @SerialName("view_booking_by_id")
            VIEW_BOOKING_BY_ID,
        }

        init {
            if (destinationType == DestinationType.VIEW_BOOKING_BY_ID) {
                Logger.e(TAG, "ERROR : Booking Id cannot be null for screen type ViewBookingById")
            }
        }
    }

    @Serializable
    data class BookingConfirmationRoute(@SerialName("booking_id") val bookingId: String) : Route()

    @Serializable
    data object ProfileRoute : Route()

    @Serializable
    data object SignInRoute : Route()

    @Serializable
    data object PhoneRoute : Route()

    @Serializable
    data object LoadingRoute : Route()

    @Serializable
    data object ProfileEditRoute : Route()

    @Serializable
    data class ServicesRoute(val serviceId: String? = null) : Route()

    @Serializable
    data object HomeRoute : Route()

    @Serializable
    data object HistoryRoute : Route()

    @Serializable
    data object FaqRoute : Route()

    // Graph Routes
    @Serializable
    data object AuthNav : Route()

    @Serializable
    data object HomeNav : Route()

    @Serializable
    data object ProfileNav : Route()

}

