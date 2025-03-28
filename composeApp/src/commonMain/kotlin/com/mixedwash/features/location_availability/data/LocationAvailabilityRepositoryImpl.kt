package com.mixedwash.features.location_availability.data

import com.mixedwash.core.domain.config.AppConfig
import com.mixedwash.core.domain.models.Result
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import com.mixedwash.features.location_availability.domain.LocationAvailabilityService
import com.mixedwash.features.location_availability.domain.model.LocationAvailabilityDTO
import com.mixedwash.libs.loki.core.calculateDistance
import kotlinx.coroutines.delay

private const val TAG = "LocationAvailabilityRepository"

class LocationAvailabilityRepositoryImpl(
    private val locationService: LocationAvailabilityService,
    private val appConfig: AppConfig
) : LocationAvailabilityRepository {

    override val bypassLocationCheck: Boolean = appConfig.bypassLocationCheck

    private suspend fun fetchLocationAvailability(): Result<LocationAvailabilityDTO> {
        return locationService.fetchLocationData()
    }

    override suspend fun isLocationServiceable(
        currentLat: Double?,
        currentLon: Double?,
        currentPincode: String
    ): Result<Boolean> {
        delay(1000)
        if (bypassLocationCheck) {
            Logger.d(TAG, "location check bypassed")
            return Result.Success(true)
        }
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
        if (currentLat != null && currentLon != null) {
            for (area in data.serviceAreas) {
                val distance =
                    calculateDistance(currentLat, currentLon, area.latitude, area.longitude)
                if (distance <= area.radiusKm) {
                    return Result.Success(true)
                }
            }
        }
        return Result.Success(false)
    }

}
