package com.mixedwash.features.location_availability.data

import com.mixedwash.core.domain.config.AppConfig
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import com.mixedwash.features.location_availability.domain.LocationAvailabilityService
import com.mixedwash.features.location_availability.domain.model.LocationAvailabilityDTO
import com.mixedwash.libs.loki.core.calculateDistance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "LocationAvailabilityRepository"

class LocationAvailabilityRepositoryImpl(
    private val locationService: LocationAvailabilityService,
    appConfig: AppConfig,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationAvailabilityRepository {

    override val bypassLocationCheck: Boolean = appConfig.bypassLocationCheck

    private suspend fun fetchLocationAvailability(): Result<LocationAvailabilityDTO> {
        return try {
            val response = locationService.fetchLocationData()
            when (response) {
                is com.mixedwash.core.domain.models.Result.Success -> Result.success(response.data)
                is com.mixedwash.core.domain.models.Result.Error -> Result.failure(
                    Exception(
                        response.error.toString()
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isLocationServiceable(
        lat: Double?,
        long: Double?,
        pincode: String
    ): Result<Boolean> = withContext(dispatcher) {
        delay(1000)
        if (bypassLocationCheck) {
            Logger.d(TAG, "location check bypassed")
            return@withContext Result.success(true)
        }

        val availabilityResponse = fetchLocationAvailability()
        if (availabilityResponse.isFailure) {
            return@withContext Result.failure(
                availabilityResponse.exceptionOrNull() ?: Exception("Unknown error")
            )
        }

        val data = availabilityResponse.getOrNull()?.data
            ?: return@withContext Result.failure(Exception("No availability data found"))

        // Check if the current pincode is serviceable.
        if (data.serviceablePincodes.contains(pincode)) {
            return@withContext Result.success(true)
        }

        // Check if the current coordinate falls within any service area.
        if (lat != null && long != null) {
            for (area in data.serviceAreas) {
                val distance = calculateDistance(lat, long, area.latitude, area.longitude)
                if (distance <= area.radiusKm) {
                    return@withContext Result.success(true)
                }
            }
        }
        return@withContext Result.success(false)
    }
}