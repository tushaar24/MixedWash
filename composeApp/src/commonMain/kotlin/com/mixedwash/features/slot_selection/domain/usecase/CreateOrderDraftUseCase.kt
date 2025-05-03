package com.mixedwash.features.slot_selection.domain.usecase

import com.mixedwash.core.feature.orders.domain.model.BookingData
import com.mixedwash.core.feature.orders.domain.model.BookingTimeSlot
import com.mixedwash.core.feature.orders.domain.model.Order
import com.mixedwash.core.feature.orders.domain.model.toBookingItem
import com.mixedwash.core.feature.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.util.addHoursToSeconds
import com.mixedwash.features.address.domain.repository.AddressRepository
import com.mixedwash.features.local_cart.domain.model.CartItem
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import com.mixedwash.features.slot_selection.domain.model.error.OrderDraftCreationException
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Use case for creating an order draft from selected slots and cart items
 */
class CreateOrderDraftUseCase(
    private val addressRepository: AddressRepository,
    private val ordersRepository: OrdersRepository,
    private val locationAvailabilityRepository: LocationAvailabilityRepository,
) {
    /**
     * Creates an order draft using the provided slot selections, cart items and delivery notes
     *
     * @param pickupSlot The selected pickup time slot
     * @param itemsByDropSlot Map of service IDs to their corresponding cart items
     * @param deliveryNotes Optional notes for delivery
     * @return Result containing the created order if successful
     */
    suspend operator fun invoke(
        pickupSlot: TimeSlot,
        itemsByDropSlot: Map<TimeSlot, List<CartItem>>,
        deliveryNotes: String
    ): Result<Order> = withContext(Dispatchers.IO) {
        runCatching {
            try {
                validateSlots(pickupSlot, itemsByDropSlot)
            } catch (e: Exception) {
                throw e
            }

            // Check if address is serviceable
            val address = addressRepository.getCurrentAddress()
                .getOrElse { throw OrderDraftCreationException.AddressNotFoundException }

            locationAvailabilityRepository.isLocationServiceable(
                lat = address.lat,
                long = address.long,
                pincode = address.pinCode
            ).onFailure {
                throw OrderDraftCreationException.AddressNotServiceableException(
                    message = "Could not check serviceability",
                    cause = it
                )
            }.onSuccess { serviceable ->
                if (!serviceable) throw OrderDraftCreationException.AddressNotServiceableException(
                    message = "Address is not serviceable",
                )
            }

            // Create booking data for each service
            val bookingDataList = itemsByDropSlot.map { (dropSlot, items) ->
                BookingData(
                    pickupSlotSelected = BookingTimeSlot(
                        id = pickupSlot.id,
                        startTimeStamp = pickupSlot.startTimeStamp,
                        endTimeStamp = pickupSlot.endTimeStamp
                    ),
                    dropSlotSelected = BookingTimeSlot(
                        id = dropSlot.id,
                        startTimeStamp = dropSlot.startTimeStamp,
                        endTimeStamp = dropSlot.endTimeStamp
                    ),
                    bookingItems = items.map { it.toBookingItem() }
                )
            }

            // Create order draft
            ordersRepository.setOrderDraft(
                bookingDataList = bookingDataList,
                deliveryNotes = deliveryNotes,
                address = address
            ).getOrThrow()
        }
    }


    /**
     * Validates the selected pickup and drop slots.
     *
     * Checks if:
     * 1. Drop slots are provided.
     * 2. The cart (items associated with drop slots) is not empty.
     * 3. Each drop slot respects the maximum required duration after the pickup slot start time for all associated items.
     *
     * @param pickupSlot The selected pickup time slot.
     * @param itemsByDropSlot A map where keys are drop time slots and values are lists of cart items associated with that drop slot.
     * @throws OrderDraftCreationException.InvalidSlotsException if no drop slots are provided or if any drop slot doesn't meet the duration requirement.
     * @throws OrderDraftCreationException.EmptyCartException if the cart is empty (no items associated with the drop slots).
     */
    private fun validateSlots(
        pickupSlot: TimeSlot,
        itemsByDropSlot: Map<TimeSlot, List<CartItem>>
    ) {
        if (itemsByDropSlot.keys.isEmpty())
            throw OrderDraftCreationException.InvalidSlotsException()

        // Check if there are any items in total across all drop slots
        if (itemsByDropSlot.values.all { it.isEmpty() })
            throw OrderDraftCreationException.EmptyCartException


        itemsByDropSlot.forEach { (dropSlot, items) ->
            // Check if any item in this drop slot violates the time constraint
            val invalidDropSlotExists = items.any { item ->
                // Ensure drop slot starts *after* the pickup slot + required duration
                dropSlot.startTimeStamp < pickupSlot.startTimeStamp.addHoursToSeconds(item.maximumDurationInHrs)
            }
            if (invalidDropSlotExists)
                throw OrderDraftCreationException.InvalidSlotsException(
                    "Drop slot does not respect maximum duration after pickup"
                )
        }
    }
}