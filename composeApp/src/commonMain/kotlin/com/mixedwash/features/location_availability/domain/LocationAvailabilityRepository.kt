package com.mixedwash.features.location_availability.domain

import com.mixedwash.core.domain.models.Result

interface LocationAvailabilityRepository {

    val bypassLocationCheck: Boolean

    suspend fun isLocationServiceable(
        currentLat: Double,
        currentLon: Double,
        currentPincode: String,
    ): Result<Boolean>

}
