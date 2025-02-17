package com.mixedwash.features.createOrder.data.local.service

import com.mixedwash.services.loki.autocomplete.Autocomplete
import com.mixedwash.services.loki.autocomplete.AutocompletePlace
import com.mixedwash.services.loki.autocomplete.AutocompleteResult
import com.mixedwash.services.loki.core.Coordinates
import com.mixedwash.services.loki.core.Location
import com.mixedwash.services.loki.core.Place
import com.mixedwash.services.loki.core.Priority
import com.mixedwash.services.loki.geocoder.Geocoder
import com.mixedwash.services.loki.geocoder.GeocoderResult
import com.mixedwash.services.loki.geolocation.Geolocator
import com.mixedwash.services.loki.geolocation.GeolocatorResult
import com.mixedwash.services.loki.geolocation.LocationRequest
import com.mixedwash.services.loki.geolocation.TrackingStatus
import com.mixedwash.services.loki.permission.LocationPermissionController
import com.mixedwash.services.loki.permission.openSettings
import kotlinx.coroutines.flow.Flow

class LocationServiceManager(
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
