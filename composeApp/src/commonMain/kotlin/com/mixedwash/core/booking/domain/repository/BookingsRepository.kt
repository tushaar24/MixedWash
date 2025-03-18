package com.mixedwash.core.booking.domain.repository

import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.core.booking.domain.model.BookingData
import com.mixedwash.core.booking.domain.model.BookingItem
import com.mixedwash.core.booking.domain.model.BookingTimeSlot

interface BookingsRepository {
    suspend fun setBookingDraft(
        pickupSlot: BookingTimeSlot,
        dropSlot: BookingTimeSlot,
        bookingItems: List<BookingItem>,
        offer: String? = null,
        deliveryNotes: String,
        address: Address
    ): Result<BookingData>
    suspend fun getDraftBooking() : Result<BookingData>
    suspend fun clearBookingDraft() :Result<BookingData?>
    suspend fun placeDraftBooking(): Result<BookingData>
    suspend fun placeBooking(bookingData: BookingData): Result<BookingData>
    suspend fun getBookings(): Result<List<BookingData>>
    suspend fun getBookingById(id: String): Result<BookingData>
}