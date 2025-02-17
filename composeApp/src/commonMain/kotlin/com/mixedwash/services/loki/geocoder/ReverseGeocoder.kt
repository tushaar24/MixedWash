package com.mixedwash.services.loki.geocoder

import com.mixedwash.services.loki.geocoder.internal.DefaultGeocoder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.mixedwash.services.loki.core.Coordinates
import com.mixedwash.services.loki.core.Place

public interface ReverseGeocoder {

    /**
     * Get the address for a given latitude and longitude.
     *
     * @param latitude The latitude to reverse geocode.
     * @param longitude The longitude to reverse geocode.
     * @return A [GeocoderResult] containing a list of addresses or an error.
     */
    public suspend fun reverse(latitude: Double, longitude: Double): GeocoderResult<Place>

    /**
     * Get the address for a given [Coordinates].
     *
     * @param coordinates The [Coordinates] to reverse geocode.
     * @return A [GeocoderResult] containing a list of addresses or an error.
     */
    public suspend fun reverse(coordinates: Coordinates): GeocoderResult<Place> =
        reverse(coordinates.latitude, coordinates.longitude)

    /**
     * Get the address for a given latitude and longitude.
     *
     * @param latitude The latitude to reverse geocode.
     * @param longitude The longitude to reverse geocode.
     * @return A [GeocoderResult] containing a list of addresses or an error.
     */
    public suspend fun places(latitude: Double, longitude: Double): GeocoderResult<Place> =
        reverse(latitude, longitude)

    /**
     * Get the address for a given [Coordinates].
     *
     * @param coordinates The [Coordinates] to reverse geocode.
     * @return A [GeocoderResult] containing a list of addresses or an error.
     */
    public suspend fun places(coordinates: Coordinates): GeocoderResult<Place> =
        reverse(coordinates)
}

/**
 * Create a new [ReverseGeocoder] using the provided [PlatformGeocoder].
 *
 * @param platformGeocoder The [PlatformGeocoder] to use for geocoding.
 * @param dispatcher The [CoroutineDispatcher] to use for geocoding.
 * @return A new [ReverseGeocoder].
 */
public fun ReverseGeocoder(
    platformGeocoder: com.mixedwash.services.loki.geocoder.PlatformGeocoder,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): ReverseGeocoder =
    com.mixedwash.services.loki.geocoder.internal.DefaultGeocoder(platformGeocoder, dispatcher)