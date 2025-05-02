package com.mixedwash.core.feature.orders.data.model

import com.mixedwash.core.feature.orders.domain.model.Booking
import com.mixedwash.core.feature.orders.domain.model.Order
import com.mixedwash.features.address.domain.model.Address


fun OrderDto.toOrder(bookings: List<Booking>) = Order(
    id = id,
    bookings = bookings,
    createdAtSeconds = createdAtSeconds,
    outForPickupSeconds = outForPickupSeconds,
    pickedUpSeconds = pickedUpSeconds,
    offers = offers,
    deliveryNotes = deliveryNotes,
    address = address
)

fun Order.toOrderDto(userId: String) = OrderDto(
    id = id,
    userId = userId,
    createdAtSeconds = createdAtSeconds,
    outForPickupSeconds = outForPickupSeconds,
    pickedUpSeconds = pickedUpSeconds,
    offers = offers,
    deliveryNotes = deliveryNotes,
    address = address
)

fun Booking.toBookingDto(
    orderId: String,
    userId: String,
    createdAtSeconds: Long,
    deliveryAddress: Address
) = BookingDto(
    id = id,
    userId = userId,
    orderId = orderId,
    createdAtSeconds = createdAtSeconds,
    deliveryAddress = deliveryAddress,
    pickupSlotSelected = pickupSlotSelected,
    dropSlotSelected = dropSlotSelected,
    bookingItems = bookingItems,
    outForDeliverySeconds = outForDeliverySeconds,
    deliveredSeconds = deliveredSeconds,
    paymentId = paymentId,
    isCancelled = isCancelled,
    activeCancellationRequestPlacedSeconds = activeCancellationRequestPlacedSeconds,
    cancellationReason = cancellationReason
)

fun BookingDto.toBooking() = Booking(
    id = id,
    pickupSlotSelected = pickupSlotSelected,
    dropSlotSelected = dropSlotSelected,
    bookingItems = bookingItems,
    outForDeliverySeconds = outForDeliverySeconds,
    deliveredSeconds = deliveredSeconds,
    paymentId = paymentId,
    isCancelled = isCancelled,
    cancellationReason = cancellationReason,
    activeCancellationRequestPlacedSeconds = activeCancellationRequestPlacedSeconds
)