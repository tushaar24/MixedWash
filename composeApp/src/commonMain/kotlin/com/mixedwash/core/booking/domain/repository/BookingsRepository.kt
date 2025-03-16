package com.mixedwash.core.booking.domain.repository

import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.local_cart.domain.model.CartItem
import com.mixedwash.features.slot_selection.data.model.TimeSlotDto
import com.mixedwash.core.booking.domain.model.BookingData

interface BookingsRepository {
    suspend fun createBooking(
        pickupSlot: TimeSlotDto,
        dropSlot: TimeSlotDto,
        cartItems: List<CartItem>,
        offer: String? = null,
        deliveryNotes: String,
        address: Address
    ): BookingData

    suspend fun getUserBookings(): Result<List<BookingData>>
    suspend fun modifyBooking() : Result<Unit>
    suspend fun cancelBooking() : Result<Unit>
}