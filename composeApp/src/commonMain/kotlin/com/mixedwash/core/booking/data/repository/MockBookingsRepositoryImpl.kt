package com.mixedwash.core.booking.data.repository

import com.mixedwash.core.booking.domain.model.BookingData
import com.mixedwash.core.booking.domain.model.BookingItem
import com.mixedwash.core.booking.domain.model.BookingState
import com.mixedwash.core.booking.domain.model.BookingTimeSlot
import com.mixedwash.core.booking.domain.model.error.BookingException
import com.mixedwash.core.booking.domain.repository.BookingsRepository
import com.mixedwash.features.address.domain.model.Address
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock

class MockBookingsRepositoryImpl : BookingsRepository {

    private var bookingDraft: BookingData? = null
    private val userBookings: MutableList<BookingData> = mutableListOf()
    private val mutex = Mutex()

    override suspend fun setBookingDraft(
        pickupSlot: BookingTimeSlot,
        dropSlot: BookingTimeSlot,
        bookingItems: List<BookingItem>,
        offer: String?,
        deliveryNotes: String,
        address: Address
    ): Result<BookingData> {
        return mutex.withLock {
            runCatching {
                bookingDraft = BookingData(
                    pickupSlotSelected = pickupSlot,
                    dropSlotSelected = dropSlot,
                    bookingItems = bookingItems,
                    deliveryNotes = deliveryNotes,
                    address = address,
                    id = "${Clock.System.now().epochSeconds}",
                    offers = null,
                    state = BookingState.Draft,
                    stateHistory = emptyList(),
                )
                bookingDraft!!
            }
        }
    }

    override suspend fun getDraftBooking(): Result<BookingData> {
        return bookingDraft?.let {
            Result.success(it)
        } ?: Result.failure(BookingException.BookingNotFound)
    }

    override suspend fun clearBookingDraft(): Result<BookingData?> {
        return mutex.withLock {
            runCatching {
                val booking = bookingDraft
                bookingDraft = null
                booking
            }
        }
    }

    override suspend fun getBookings(): Result<List<BookingData>> {
        return Result.success(userBookings)
    }

    override suspend fun getBookingById(id: String): Result<BookingData> {
        return userBookings.find { it.id == id }?.let {
            Result.success(it)
        } ?: Result.failure(BookingException.BookingNotFound)
    }

    override suspend fun placeDraftBooking(): Result<BookingData> {
        return runCatching {
            bookingDraft?.let {
                val booking = it.copy(
                    state = BookingState.Initiated(
                        placedAtMillis = Clock.System.now().toEpochMilliseconds()
                    )
                )
                userBookings.add(booking)
                booking
            } ?: throw BookingException.BookingNotFound
        }
    }

    override suspend fun placeBooking(bookingData: BookingData): Result<BookingData> {
        return runCatching {
            val booking = bookingData.copy(
                state = BookingState.Initiated(
                    placedAtMillis = Clock.System.now().toEpochMilliseconds()
                )
            )
            userBookings.add(booking)
            booking
        }
    }

}