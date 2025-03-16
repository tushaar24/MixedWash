package com.mixedwash.core.booking.data.repository

import com.mixedwash.core.booking.domain.model.toBookingItem
import com.mixedwash.core.booking.domain.repository.BookingsRepository
import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.local_cart.domain.model.CartItem
import com.mixedwash.features.slot_selection.data.model.TimeSlotDto
import com.mixedwash.core.booking.domain.model.BookingData

class BookingsRepositoryImpl : BookingsRepository {
    override suspend fun createBooking(
        pickupSlot: TimeSlotDto,
        dropSlot: TimeSlotDto,
        cartItems: List<CartItem>,
        offer: String?,
        deliveryNotes: String,
        address: Address
    ): BookingData {
        return BookingData(
            pickupSlot = pickupSlot,
            dropSlot = dropSlot,
            bookingItems = cartItems.map { it.toBookingItem() },
            offer = offer,
            deliveryNotes = deliveryNotes,
            address = address
        )
    }

    override suspend fun getUserBookings(): Result<List<BookingData>> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyBooking(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun cancelBooking(): Result<Unit> {
        TODO("Not yet implemented")
    }
}