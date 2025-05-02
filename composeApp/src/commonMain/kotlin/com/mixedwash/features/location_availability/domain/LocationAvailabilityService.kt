package com.mixedwash.features.location_availability.domain

import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.location_availability.domain.model.LocationAvailabilityDTO

interface LocationAvailabilityService {
    suspend fun fetchLocationData(): Result<LocationAvailabilityDTO>
}