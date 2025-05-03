package com.mixedwash.features.order_details.presentation

import com.mixedwash.core.feature.orders.domain.model.Order
import com.mixedwash.core.presentation.models.SnackbarPayload

data class OrderDetailsScreenState(
    val order: Order? = null,
    val serviceImageUrls: Map<String, String> = emptyMap(),
    val stagingEnabled: Boolean = false,
    val stagingOperations: List<StagingOperationType> = StagingOperationType.entries,
    val isRefreshing: Boolean = false,
)

enum class StagingOperationType(val title: String) {
    DELETE("Delete Order"),
    CANCEL("Cancel Booking"),
    SET_OUT_FOR_PICKUP("Set Order Out For Pickup"),
    SET_PICKED_UP("Set Order Picked Up"),
    SET_OUT_FOR_DELIVERY("Set Booking Out For Delivery"),
    SET_DELIVERED("Set Booking Delivered"),
    SET_PAID("Set Booking Paid")
}

sealed class OrderDetailsScreenEvent {
    data object Refresh: OrderDetailsScreenEvent()
    data class OnCancelBooking(val bookingId: String): OrderDetailsScreenEvent()
    data class OnDeleteOrder(val orderId: String): OrderDetailsScreenEvent()
    data class OnSetOutForPickup(val orderId: String): OrderDetailsScreenEvent()
    data class OnSetPickedUp(val orderId: String): OrderDetailsScreenEvent()
    data class OnSetOutForDelivery(val bookingId: String): OrderDetailsScreenEvent()
    data class OnSetDelivered(val bookingId: String): OrderDetailsScreenEvent()
    data class OnSetPaid(val bookingId: String): OrderDetailsScreenEvent()
}

sealed class OrderDetailsScreenUiEvent {
    data class ShowSnackbar(val payload: SnackbarPayload): OrderDetailsScreenUiEvent()
}