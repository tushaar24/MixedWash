package com.mixedwash.features.location_availability.data

import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import com.mixedwash.features.location_availability.domain.model.LocationAvailabilityDTO
import com.mixedwash.libs.loki.core.calculateDistance

class LocationAvailabilityRepositoryImpl(private val locationService: FirebaseLocationAvailabilityService) :
    LocationAvailabilityRepository {

    private suspend fun fetchLocationAvailability(): Result<LocationAvailabilityDTO> {
        return locationService.fetchLocationData()
    }

    override suspend fun isLocationServiceable(
        currentLat: Double,
        currentLon: Double,
        currentPincode: String
    ): Result<Boolean> {
        val availabilityResponse = fetchLocationAvailability()
        if (availabilityResponse is Result.Error) {
            return availabilityResponse
        }
        val data = (availabilityResponse as Result.Success).data.data

        // Check if the current pincode is serviceable.
        if (data.serviceablePincodes.contains(currentPincode)) {
            return Result.Success(true)
        }

        // Check if the current coordinate falls within any service area.
        for (area in data.serviceAreas) {
            val distance = calculateDistance(currentLat, currentLon, area.latitude, area.longitude)
            if (distance <= area.radiusKm) {
                return Result.Success(true)
            }
        }
        return Result.Success(false)
    }

}
