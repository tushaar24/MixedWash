package com.mixedwash.features.location_availability.data

import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.features.location_availability.domain.model.LocationAvailabilityDTO
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import com.mixedwash.core.domain.models.Result

private const val COLLECTION_API_LOCATION_AVAILABILITY = "API_LOCATION_AVAILABILITY"
private const val DOCUMENT_LOCATION_DATA = "location_data"

class FirebaseLocationAvailabilityService() {

    private val db = Firebase.firestore
    suspend fun fetchLocationData(): Result<LocationAvailabilityDTO> {
        return try{
            val dto = db.collection(COLLECTION_API_LOCATION_AVAILABILITY)
                .document(DOCUMENT_LOCATION_DATA)
                .get()
                .data<LocationAvailabilityDTO>()
            Result.Success(dto)
        } catch (e: Exception) {
            if(e is CancellationException) {
                throw e
            }
            else {
               e.printStackTrace()
                Result.Error(ErrorType.Unknown("Failed to fetch location availability: ${e.message}"))
            }
        }
    }
}
