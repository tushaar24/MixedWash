package com.mixedwash.core.booking.data.repository

import com.mixedwash.core.booking.domain.model.BookingData
import com.mixedwash.core.booking.domain.model.BookingItem
import com.mixedwash.core.booking.domain.model.BookingItemPricing
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
    val now = Clock.System.now().toEpochMilliseconds()
    private val userBookings: MutableList<BookingData> = mutableListOf<BookingData>(
        BookingData(
            id = "10245780",
            pickupSlotSelected = BookingTimeSlot(id = 1234, startTimeStamp = now, endTimeStamp = now),
            dropSlotSelected = BookingTimeSlot(id = 1245, startTimeStamp = now, endTimeStamp = now),
            bookingItems = listOf(
                BookingItem(
                    itemId = "14523",
                    name = "Dry Clean",
                    serviceName = "Dry Clean",
                    quantity = 5,
                    itemPricing = BookingItemPricing.SubItemFixedPricing(150),
                    serviceId = "1",
                    imageUrl = "",
                    createdMillis = now
                ),

                BookingItem(
                    itemId = "14523",
                    name = "Dry Clean",
                    serviceName = "Shoe Clean",
                    quantity = 5,
                    itemPricing = BookingItemPricing.SubItemFixedPricing(150),
                    serviceId = "1",
                    imageUrl = "",
                    createdMillis = now
                ),

                BookingItem(
                    itemId = "14523",
                    name = "Dry Clean",
                    serviceName = "Wash and Fold",
                    quantity = 5,
                    itemPricing = BookingItemPricing.SubItemFixedPricing(150),
                    serviceId = "1",
                    imageUrl = "",
                    createdMillis = now
                )
            ),
            offers = null,
            deliveryNotes = "",
            address = Address(title = "My Home", addressLine1 = "Washington DC", pinCode = 231217.toString()),
            state = BookingState.Processing(now),
            stateHistory = emptyList()
        )
    )
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