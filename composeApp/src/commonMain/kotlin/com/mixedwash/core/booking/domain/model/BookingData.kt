package com.mixedwash.core.booking.domain.model

import com.mixedwash.features.address.domain.model.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingData(
    @SerialName("id")
    val id: String,
    @SerialName("pickup_slot")
    val pickupSlotSelected: BookingTimeSlot,
    @SerialName("drop_slot")
    val dropSlotSelected: BookingTimeSlot,
    @SerialName("booking_items")
    val bookingItems: List<BookingItem>,
    @SerialName("offers")
    val offers: List<BookingOffer>? = null,
    @SerialName("delivery_notes")
    val deliveryNotes: String,
    @SerialName("address")
    val address: Address,
    @SerialName("state")
    val state: BookingState,
    @SerialName("state_history")
    val stateHistory: List<StateEvent> = emptyList()
)

@Serializable
sealed class BookingState {
    @Serializable
    @SerialName("draft")
    data object Draft : BookingState()

    @Serializable
    @SerialName("initiated")
    data class Initiated(
        @SerialName("placed_at_millis") val placedAtMillis: Long,
    ) : BookingState()

    @Serializable
    @SerialName("out_for_pickup")
    data class OutForPickup(
        @SerialName("driver_info")
        val driverInfo: DriverInfo,
        @SerialName("assigned_at_millis")
        val assignedAtMillis: Long,
        @SerialName("is_delayed")
        val isDelayed: Boolean,
        @SerialName("reason_for_delay")
        val reasonForDelay: String? = null
    ) : BookingState()

    @Serializable
    @SerialName("picked_up")
    data class PickedUp(
        @SerialName("actual_pickup_time")
        val actualPickupTime: Long
    ) : BookingState()

    @Serializable
    @SerialName("processing")
    data class Processing(
        @SerialName("started_processing_at_millis")
        val startedProcessingAtMillis: Long,
        @SerialName("processing_delayed")
        val processingDelayed: Boolean = false,
        @SerialName("processing_delay_reason")
        val processingDelayReason: String? = null
    ) : BookingState()

    @Serializable
    @SerialName("processed")
    data class Processed(
        @SerialName("processed_items")
        val processedItems: List<BookingItem>,
    ) : BookingState()

    @Serializable
    @SerialName("out_for_delivery")
    data class OutForDelivery(
        @SerialName("driver_info")
        val driverInfo: DriverInfo,
        @SerialName("delivery_start_time")
        val deliveryStartTime: Long,
        @SerialName("delivery_delayed")
        val deliveryDelayed: Boolean = false,
        @SerialName("delivery_delay_reason")
        val deliveryDelayReason: String? = null,

        ) : BookingState()

    @Serializable
    @SerialName("delivered")
    data class Delivered(
        @SerialName("actual_delivery_time")
        val actualDeliveryTime: Long,
        @SerialName("is_late")
        val isLate: Boolean,
        @SerialName("late_reason")
        val lateReason: String? = null
    ) : BookingState()

    @Serializable
    @SerialName("cancelled")
    data class Cancelled(
        @SerialName("cancelled_at_millis")
        val cancelledAtMillis: Long,
        @SerialName("cancellation_reason")
        val cancellationReason: String
    ) : BookingState()
}

// An event log to record state transitions:
@Serializable
data class StateEvent(
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("state")
    val state: BookingState,
    @SerialName("note")
    val note: String? = null
)

// Driver info model:
@Serializable
data class DriverInfo(
    @SerialName("driver_id")
    val driverId: String,
    @SerialName("driver_name")
    val driverName: String,
    @SerialName("contact_number")
    val contactNumber: String
)
