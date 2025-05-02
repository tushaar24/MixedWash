package com.mixedwash.features.location_availability.domain

interface LocationAvailabilityRepository {

    val bypassLocationCheck: Boolean

    suspend fun isLocationServiceable(
        lat: Double?,
        long: Double?,
        pincode: String,
    ): Result<Boolean>

}