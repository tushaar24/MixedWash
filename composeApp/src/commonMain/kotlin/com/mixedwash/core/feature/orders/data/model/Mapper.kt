package com.mixedwash.core.feature.orders.data.model

import com.mixedwash.core.feature.orders.domain.model.Booking
import com.mixedwash.core.feature.orders.domain.model.Order


fun com.mixedwash.core.feature.orders.data.model.OrderDto.toOrder(bookings: List<Booking>) = Order(
    id = id,
    bookings = bookings,
    createdAtSeconds = createdAtSeconds,
    outForPickupSeconds = outForPickupSeconds,
    pickedUpSeconds = pickedUpSeconds,
    offers = offers,
    deliveryNotes = deliveryNotes,
    address = address
)

fun Order.toOrderDto(userId: String) = com.mixedwash.core.feature.orders.data.model.OrderDto(
    id = id,
    userId = userId,
    createdAtSeconds = createdAtSeconds,
    outForPickupSeconds = outForPickupSeconds,
    pickedUpSeconds = pickedUpSeconds,
    offers = offers,
    deliveryNotes = deliveryNotes,
    address = address
)

fun Booking.toBookingDto(orderId: String, userId: String, createdAtSeconds: Long) = BookingDto(
    id = id,
    userId = userId,
    orderId = orderId,
    pickupSlotSelected = pickupSlotSelected,
    dropSlotSelected = dropSlotSelected,
    bookingItems = bookingItems,
    outForDeliverySeconds = outForDeliverySeconds,
    deliveredSeconds = deliveredSeconds,
    isPaid = isPaid,
    isCancelled = isCancelled,
    cancellationReason = cancellationReason,
    createdAtSeconds = createdAtSeconds
)

fun BookingDto.toBooking() = Booking(
    id = id,
    pickupSlotSelected = pickupSlotSelected,
    dropSlotSelected = dropSlotSelected,
    bookingItems = bookingItems,
    outForDeliverySeconds = outForDeliverySeconds,
    deliveredSeconds = deliveredSeconds,
    isPaid = isPaid,
    isCancelled = isCancelled,
    cancellationReason = cancellationReason,
)