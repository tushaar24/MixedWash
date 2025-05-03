package com.mixedwash.core.feature.orders.domain.repository

import com.mixedwash.core.feature.orders.domain.error.OrderException
import com.mixedwash.core.feature.orders.domain.model.Booking
import com.mixedwash.core.feature.orders.domain.model.BookingData
import com.mixedwash.core.feature.orders.domain.model.Order
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
     * @param bookingDataList The list of booking data for the order
     * @param offer Optional offer to apply to the order
     * @param deliveryNotes Notes for delivery
     * @param address The delivery address
     * @return Result containing the created order draft or an error
     */
    suspend fun setOrderDraft(
        bookingDataList: List<BookingData>,
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
     * @return Result containing Unit on success or an error
     */
    suspend fun clearOrderDraft(): Result<Unit>

    /**
     * Places the current draft order.
     *
     * @return Result containing Unit on success or an error
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
     * @return Result containing Unit on success or an error
     */
    suspend fun setOrderOutForPickup(orderId: String): Result<Unit>

    /**
     * Sets an order as picked up.
     *
     * @param orderId The ID of the order to update
     * @return Result containing Unit on success or an error
     */
    suspend fun setOrderPickedUp(orderId: String): Result<Unit>

    /**
     * Sets an order as out for delivery.
     *
     * @param bookingId The ID of the booking
     * @return Result containing Unit on success or an error
     */
    suspend fun setBookingOutForDelivery(bookingId: String): Result<Unit>

    /**
     * Sets an order as delivered.
     *
     * @param bookingId The ID of the booking
     * @return Result containing Unit on success or an error
     */
    suspend fun setBookingDelivered(bookingId: String): Result<Unit>

    /**
     * Sets a booking as paid.
     *
     * @param bookingId The ID of the booking
     * @param isPaid Whether the booking is paid
     * @return Result containing Unit on success or an error
     */
    suspend fun setBookingPaid(bookingId: String, isPaid: Boolean): Result<Unit>

    /**
     * Cancels a booking by its ID, marking it as cancelled by the user.
     *
     * This function updates the booking to record that it has been cancelled, including setting the cancellation timestamp
     * and the reason for cancellation. The operation returns a Result indicating success or failure.
     *
     * @param bookingId The unique identifier of the booking to cancel.
     * @return A Result<Unit> signifying the outcome of the cancellation operation.
     */
    suspend fun setBookingCancelled(bookingId: String): Result<Unit>


    /**
     * Clears all staging orders.
     * Only available when using the staging collection.
     *
     * @return Result containing Unit on success or an error
     * @throws OrderException.IllegalStagingOperationException if not using the staging collection
     */
    suspend fun clearAllOrders(): Result<Unit>
    

    /**
     * Fetches a list of active bookings.
     *
     * Active bookings are those that have not yet been marked as delivered.
     * Each entry in the returned list pairs an order ID with its associated active booking.
     *
     * @return Result containing a list of (order ID, Booking) pairs on success, or an error if the operation fails.
     */
    suspend fun fetchActiveBookings(): Result<List<Pair<String, Booking>>>
}