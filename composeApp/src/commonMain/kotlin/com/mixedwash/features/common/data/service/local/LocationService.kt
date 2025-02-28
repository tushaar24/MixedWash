package com.mixedwash.features.common.data.service.local

import com.mixedwash.libs.loki.autocomplete.Autocomplete
import com.mixedwash.libs.loki.autocomplete.AutocompletePlace
import com.mixedwash.libs.loki.autocomplete.AutocompleteResult
import com.mixedwash.libs.loki.core.Coordinates
import com.mixedwash.libs.loki.core.Location
import com.mixedwash.libs.loki.core.Place
import com.mixedwash.libs.loki.core.Priority
import com.mixedwash.libs.loki.geocoder.Geocoder
import com.mixedwash.libs.loki.geocoder.GeocoderResult
import com.mixedwash.libs.loki.geolocation.Geolocator
import com.mixedwash.libs.loki.geolocation.GeolocatorResult
import com.mixedwash.libs.loki.geolocation.LocationRequest
import com.mixedwash.libs.loki.geolocation.TrackingStatus
import com.mixedwash.libs.loki.permission.LocationPermissionController
import com.mixedwash.libs.loki.permission.openSettings
import kotlinx.coroutines.flow.Flow

class LocationService(
    private val geolocator: Geolocator,
    private val geocoder: Geocoder,
    private val autocomplete: Autocomplete<AutocompletePlace>,
) {

    val trackingStatus: Flow<TrackingStatus> = geolocator.trackingStatus

    suspend fun getCurrentLocation(): GeolocatorResult {
        return geolocator.current(Priority.HighAccuracy)
    }

    suspend fun isLocationEnabled(): Boolean {
        return geolocator.isAvailable()
    }

    fun openSettings() {
        LocationPermissionController.openSettings()
    }

    suspend fun getPlaceByCoordinates(coordinates: Coordinates): GeocoderResult<Place> =
        geocoder.reverse(coordinates)

    suspend fun getCoordinatesByPlacedId(placeId: String): GeocoderResult<Coordinates> {
        return geocoder.forward(placeId)
    }

    suspend fun getPlaceByPlaceId(placeId: String): GeocoderResult<Place> {
        val coordinatesResult = geocoder.forward(placeId)
        return if (coordinatesResult !is GeocoderResult.Success) {
            GeocoderResult.GeocodeFailed(message = "Lookup Failed")
        } else {
            geocoder.reverse(coordinates = coordinatesResult.data.first())
        }
    }

    suspend fun searchAutoComplete(query: String): AutocompleteResult<AutocompletePlace> {
        return autocomplete.search(query)
    }

    fun startTracking() {
        geolocator.track(LocationRequest(priority = Priority.HighAccuracy))
    }

    fun stopTracking() {
        geolocator.stopTracking()
    }

    suspend fun getPlace(location: Location): GeocoderResult<Place> {
        return geocoder.reverse(location.coordinates)
    }
}
