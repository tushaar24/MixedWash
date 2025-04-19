package com.mixedwash.core.orders.domain.repository

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.error.OrderException
import com.mixedwash.features.address.domain.model.Address

interface OrdersRepository {
    /**
     * Flag indicating whether the repository is using a staging collection.
     * This collection is used for testing and development purposes, and allows for operations that would not be permitted in a production environment.
     */
    val isStagingEnabled: Boolean

    /**
     * Sets an order draft with the provided details.
     *
     * @param userId The ID of the user creating the draft
     * @param bookingsData The list of booking data for the order
     * @param offer Optional offer to apply to the order
     * @param deliveryNotes Notes for delivery
     * @param address The delivery address
     * @return Result containing the created order draft or an error
     */
    suspend fun setOrderDraft(
        userId: String,
        bookingsData: List<BookingData>,
        offer: String? = null,
        deliveryNotes: String,
        address: Address
    ): Result<Order>

    /**
     * Gets the current order draft.
     *
     * @return Result containing the current order draft or an error
     */
    suspend fun getOrderDraft() : Result<Order>

    /**
     * Clears the current order draft.
     *
     * @return Result containing the cleared order draft or null
     */
    suspend fun clearOrderDraft() :Result<Order?>

    /**
     * Places the current draft order.
     *
     * @return Result containing the placed order or an error
     */
    suspend fun placeDraftOrder(): Result<Order>

    /**
     * Gets all orders sorted by most recent first.
     *
     * @return Result containing a list of orders or an error
     */
    suspend fun getAllOrdersMostRecentFirst(): Result<List<Order>>

    /**
     * Gets an order by its ID.
     *
     * @param id The ID of the order to retrieve
     * @return Result containing the order or an error
     */
    suspend fun getOrderById(id: String): Result<Order>

    /**
     * Gets an order by a booking ID within the order.
     *
     * @param bookingId The ID of the booking to find the order for
     * @return Result containing the order or an error
     */
    suspend fun getOrderByBookingId(bookingId: String): Result<Order>

    /**
     * Deletes an order by its ID.
     * Only available when using the staging collection.
     *
     * @param orderId The ID of the order to delete
     * @return Result containing Unit on success or an error
     * @throws OrderException.IllegalStagingOperationException if not using the staging collection
     */
    suspend fun deleteOrder(orderId: String): Result<Unit>

    /**
     * Sets an order as out for pickup.
     *
     * @param orderId The ID of the order to update
     * @return Result containing the updated order or an error
     */
    suspend fun setOrderOutForPickup(orderId: String): Result<Order>

    /**
     * Sets an order as picked up.
     *
     * @param orderId The ID of the order to update
     * @return Result containing the updated order or an error
     */
    suspend fun setOrderPickedUp(orderId: String): Result<Order>

    /**
     * Sets an order as out for delivery.
     *
     * @param orderId The ID of the order to update
     * @param bookingId The ID of the booking
     * @return Result containing the updated order or an error
     */
    suspend fun setBookingOutForDelivery(orderId: String, bookingId: String): Result<Order>

    /**
     * Sets an order as delivered.
     *
     * @param orderId The ID of the order to update
     * @param bookingId The ID of the booking
     * @return Result containing the updated order or an error
     */
    suspend fun setBookingDelivered(orderId: String, bookingId: String): Result<Order>

    /**
     * Sets a booking as paid.
     *
     * @param orderId The ID of the order to update
     * @param bookingId The ID of the booking
     * @param isPaid Whether the booking is paid
     * @return Result containing the updated order or an error
     */
    suspend fun setBookingPaid(orderId: String, bookingId: String, isPaid: Boolean): Result<Order>

    /**
     * Clears all staging orders.
     * Only available when using the staging collection.
     *
     * @return Result containing Unit on success or an error
     * @throws OrderException.IllegalStagingOperationException if not using the staging collection
     */
    suspend fun clearAllOrders(): Result<Unit>

    suspend fun fetchActiveBookings(): Result<List<Pair<String, Booking>>>
}