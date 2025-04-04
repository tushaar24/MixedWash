package com.mixedwash.features.order_details.presentation

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.BookingItem
import com.mixedwash.core.orders.domain.model.BookingTimeSlot
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.address.domain.model.Address

data class OrderDetailsScreenState(
    val title: String,
    val note: String?=null,
    val bookings: List<Booking>,
    val deliveryAddress: Address?,
    val screenType: OrderDetailsScreenType = OrderDetailsScreenType.ORDER_DETAILS
)

enum class OrderDetailsScreenType {
    ORDER_DETAILS,
    CONFIRMATION
}

sealed class OrderDetailsScreenEvent {
    data object OnPlaceOrder : OrderDetailsScreenEvent()
}

sealed class OrderDetailsScreenUiEvent {
    data class ShowSnackbar(val snackbarPayload: SnackbarPayload) : OrderDetailsScreenUiEvent()
    data class NavigateToOrderConfirmation(val orderId: String) : OrderDetailsScreenUiEvent()
}