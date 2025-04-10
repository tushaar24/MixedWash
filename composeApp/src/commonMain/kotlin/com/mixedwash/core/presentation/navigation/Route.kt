package com.mixedwash.core.presentation.navigation

import com.mixedwash.core.presentation.util.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

private const val TAG = "Route"

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class Route {

    @Serializable
    @SerialName("address_route")
    data class AddressRoute(
        val title: String,
        val screenType: ScreenType,
        val submitText: String,
        val onSubmitNavArgsSerialized: String? = null
    ) : Route() {
        @Serializable
        enum class ScreenType {
            @SerialName("Edit")
            Edit,

            @SerialName("SelectAddress")
            SelectAddress
        }

        init {
             if (screenType == ScreenType.SelectAddress && submitText.isEmpty())
         {

             Logger.e(TAG, "ERROR: Submit text cannot be null for screen type SelectAddress")
            }
        }
    }

    @Serializable
    @SerialName("slot_selection_route")
    data object SlotSelectionRoute : Route()



    @SerialName("booking_details_route")
    @Serializable
    data class OrderDetailsRoute(
        val bookingId: String? = null,
        val destinationType: DestinationType
    ) : Route() {
        @Serializable
        enum class DestinationType {
            @SerialName("confirm_draft_booking")
            CONFIRM_DRAFT_ORDER,

            @SerialName("view_booking_by_id")
            VIEW_ORDER_BY_BOOKING_ID,
        }

        init {
            if (destinationType == DestinationType.VIEW_ORDER_BY_BOOKING_ID && bookingId.isNullOrEmpty()) {
                Logger.e(
                    TAG,
                    "ERROR: Booking ID cannot be null or empty for screen type ViewBookingById"
                )
            }
        }
    }

    @Serializable
    @SerialName("order_confirmation_route")
    data class OrderConfirmationRoute(
        @SerialName("booking_id") val bookingId: String
    ) : Route()

    @Serializable
    @SerialName("profile_route")
    data object ProfileRoute : Route()

    @Serializable
    @SerialName("sign_in_route")
    data object SignInRoute : Route()

    @Serializable
    @SerialName("phone_route")
    data object PhoneRoute : Route()

    @Serializable
    @SerialName("loading_route")
    data object LoadingRoute : Route()

    @Serializable
    @SerialName("profile_edit_route")
    data object ProfileEditRoute : Route()

    @Serializable
    @SerialName("services_route")
    data class ServicesRoute(val serviceId: String? = null) : Route()

    @Serializable
    @SerialName("home_route")
    data object HomeRoute : Route()

    @Serializable
    @SerialName("history_route")
    data object HistoryRoute : Route()

    @Serializable
    @SerialName("faq_route")
    data object FaqRoute : Route()

    // Graph Routes
    @Serializable
    @SerialName("auth_nav")
    data object AuthNav : Route()

    @Serializable
    @SerialName("home_nav")
    data object HomeNav : Route()

    @Serializable
    @SerialName("profile_nav")
    data object ProfileNav : Route()

    @Serializable
    @SerialName("onboarding_route")
    data object OnboardingRoute : Route()
}