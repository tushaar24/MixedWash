package com.mixedwash.features.booking_details.presentation

import com.mixedwash.core.booking.domain.model.BookingItem
import com.mixedwash.core.booking.domain.model.BookingTimeSlot
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.common.presentation.address.model.Address

data class BookingDetailsScreenState(
    val title: String,
    val note: String?=null,
    val items: List<BookingItem>,
    val pickupSlot: BookingTimeSlot?,
    val dropSlot: BookingTimeSlot?,
    val deliveryAddress: Address?,
    val screenType: BookingDetailsScreenType = BookingDetailsScreenType.BOOKING_DETAILS
)

enum class BookingDetailsScreenType {
    BOOKING_DETAILS,
    CONFIRMATION
}

sealed class BookingDetailsScreenEvent {
    data object OnConfirmBooking : BookingDetailsScreenEvent()
}

sealed class BookingDetailsScreenUiEvent {
    data class ShowSnackbar(val snackbarPayload: SnackbarPayload) : BookingDetailsScreenUiEvent()
    data class NavigateToBookingConfirmation(val bookingId: String) : BookingDetailsScreenUiEvent()
}