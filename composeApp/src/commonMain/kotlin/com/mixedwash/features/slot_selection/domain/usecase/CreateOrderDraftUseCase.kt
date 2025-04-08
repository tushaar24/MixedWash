package com.mixedwash.features.slot_selection.domain.usecase

import com.mixedwash.core.data.UserService
import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.core.orders.domain.model.BookingTimeSlot
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.toBookingItem
import com.mixedwash.core.orders.domain.repository.OrdersRepository
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
    private val userService: UserService
) {
    /**
     * Creates an order draft using the provided slot selections, cart items and delivery notes
     *
     * @param pickupTimeSlot The selected pickup time slot
     * @param cartItemsByServiceId Map of service IDs to their corresponding cart items
     * @param dropTimeSlotsByServiceId Map of service IDs to their corresponding drop time slots
     * @param deliveryNotes Optional notes for delivery
     * @return Result containing the created order if successful
     */
    suspend operator fun invoke(
        pickupTimeSlot: TimeSlot,
        cartItemsByServiceId: Map<String, List<CartItem>>,
        dropTimeSlotsByServiceId: Map<String, TimeSlot>,
        deliveryNotes: String
    ): Result<Order> = withContext(Dispatchers.IO) {
        // Get the current user ID
        val userId = userService.currentUser?.uid ?: ""

        if (cartItemsByServiceId.isEmpty()) {
            return@withContext Result.failure(OrderDraftCreationException.EmptyCartException)
        }
        
        // Ensure all services have drop slots
        val missingServiceDropSlots = cartItemsByServiceId.keys.any { !dropTimeSlotsByServiceId.containsKey(it) }
        if (missingServiceDropSlots) {
            return@withContext Result.failure(OrderDraftCreationException.InvalidSlotsException)
        }
        
        // Get current address
        val address = addressRepository.getCurrentAddress()
            .getOrElse { return@withContext Result.failure(OrderDraftCreationException.AddressNotFoundException) }
        
        // Check if address is serviceable
        val isServiceable = locationAvailabilityRepository.isLocationServiceable(
            lat = address.lat,
            long = address.long,
            pincode = address.pinCode
        ).getOrNull() ?: false
        
        if (!isServiceable) {
            return@withContext Result.failure(OrderDraftCreationException.AddressNotServiceableException)
        }
        
        // Create booking data for each service
        val bookingsData = cartItemsByServiceId.map { (serviceId, items) ->
            val dropTimeSlot = dropTimeSlotsByServiceId[serviceId]
                ?: return@withContext Result.failure(OrderDraftCreationException.InvalidSlotsException)
                
            // Validate that drop time is after pickup time
            if (dropTimeSlot.startTimeStamp <= pickupTimeSlot.endTimeStamp) {
                return@withContext Result.failure(OrderDraftCreationException.InvalidSlotsException)
            }
            
            BookingData(
                pickupSlotSelected = BookingTimeSlot(
                    id = pickupTimeSlot.id,
                    startTimeStamp = pickupTimeSlot.startTimeStamp,
                    endTimeStamp = pickupTimeSlot.endTimeStamp
                ),
                dropSlotSelected = BookingTimeSlot(
                    id = dropTimeSlot.id,
                    startTimeStamp = dropTimeSlot.startTimeStamp,
                    endTimeStamp = dropTimeSlot.endTimeStamp
                ),
                bookingItems = items.map { it.toBookingItem() }
            )
        }
        
        // Create order draft
        return@withContext ordersRepository.setOrderDraft(
            userId = userId,
            bookingsData = bookingsData,
            deliveryNotes = deliveryNotes,
            address = address
        )
    }
}