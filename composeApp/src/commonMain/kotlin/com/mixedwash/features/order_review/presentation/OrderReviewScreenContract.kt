package com.mixedwash.features.order_review.presentation

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.address.domain.model.Address

data class OrderReviewScreenState(
    val title: String,
    val note: String?=null,
    val bookings: List<Booking>,
    val deliveryAddress: Address?,
    val screenType: OrderReviewScreenType = OrderReviewScreenType.ORDER_DETAILS
)

enum class OrderReviewScreenType {
    ORDER_DETAILS,
    CONFIRMATION
}

sealed class OrderReviewScreenEvent {
    data object OnPlaceOrder : OrderReviewScreenEvent()
}

sealed class OrderReviewScreenUiEvent {
    data class ShowSnackbar(val snackbarPayload: SnackbarPayload) : OrderReviewScreenUiEvent()
    data class NavigateToOrderConfirmation(val orderId: String) : OrderReviewScreenUiEvent()
}