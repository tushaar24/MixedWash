package com.mixedwash.features.services.domain

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.features.services.data.remote.model.ServiceDto
import com.mixedwash.features.services.data.remote.model.ServiceItemDto
import com.mixedwash.features.services.data.remote.model.ServicesResponseDto

interface ServicesDataRepository {
    /**
     * Fetches all available services
     * @return Result containing the services response or an error
     */
    suspend fun getAllServices() : Result<ServicesResponseDto>

    /**
     * Fetches a service by its ID
     * @param id the service id
     * @return Result containing the service if found or an error
     */
    suspend fun getServiceById(id: String): Result<ServiceDto>

    /**
     * Fetches a service item by its ID
     * @param id the item id
     * @return Result containing the service item if found or an error
     */
    suspend fun getServiceItemById(id: String): Result<ServiceItemDto>

    /**
     * Fetches a service that contains the specified service item ID
     * @param itemId the service item id
     * @return Result containing the parent service if found or an error
     */
    suspend fun getServiceByServiceItemId(itemId: String): Result<ServiceDto>

    /**
     * A single booking contains multiple booking items. Each booking item is associated with a service id.
     * This function fetches the service associated with the first booking item in the booking.
     * @param booking the booking containing the booking items
     * @return Result containing the service if found or an error
     */
    suspend fun getServiceForBooking(booking: Booking): Result<ServiceDto>

    /**
     * Maps all services to their image URLs.
     * @return Result containing a map of service IDs to their image URLs or an error
     */
    suspend fun mapAllServicesToImageUrls(): Result<Map<String, String>>
}