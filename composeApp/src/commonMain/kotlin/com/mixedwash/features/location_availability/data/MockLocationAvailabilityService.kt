package com.mixedwash.features.location_availability.data

import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.location_availability.domain.LocationAvailabilityService
import com.mixedwash.features.location_availability.domain.model.LocationAvailabilityDTO
import kotlinx.serialization.json.Json
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi


private const val filePath = "files/mock/location_availability_data.json"

class MockLocationAvailabilityService() : LocationAvailabilityService {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun fetchLocationData(): Result<LocationAvailabilityDTO> {
        val bytes = Res.readBytes(filePath)
        val string = bytes.decodeToString()
        return try {
            Result.Success(Json.decodeFromString<LocationAvailabilityDTO>(string))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(ErrorType.Unknown(e.message?:"Error Fetching Mock Data"))
        }
    }

}