package com.mixedwash.libs.loki.geocoder

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.mixedwash.libs.loki.core.Coordinates

public interface ForwardGeocoder {

    /**
     * Get a list of [Coordinates]s for a given address.
     *
     * In most cases, the list will contain a single [Coordinates]. However, in some cases, it may
     * return multiple [Coordinates]s.
     *
     * @param placeId The placeId of the address to geocode.
     * @return A [GeocoderResult] containing the list of [Coordinates]s or an error.
     */
    public suspend fun forward(placeId: String): GeocoderResult<Coordinates>

    /**
     * Get a list of [Coordinates]s for a given address.
     *
     * In most cases, the list will contain a single [Coordinates]. However, in some cases, it may
     * return multiple [Coordinates]s.
     *
     * @param placeId The placeId of the address to geocode.
     * @return A [GeocoderResult] containing the list of [Coordinates]s or an error.
     */
    public suspend fun locations(placeId: String): GeocoderResult<Coordinates> = forward(placeId)
}

/**
 * Create a new [ForwardGeocoder] using the provided [PlatformGeocoder].
 *
 * @param platformGeocoder The [PlatformGeocoder] to use for geocoding.
 * @param dispatcher The [CoroutineDispatcher] to use for running the geocoding operations.
 * @return A new [ForwardGeocoder].
 */
public fun ForwardGeocoder(
    platformGeocoder: com.mixedwash.libs.loki.geocoder.PlatformGeocoder,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): ForwardGeocoder =
    com.mixedwash.libs.loki.geocoder.internal.DefaultGeocoder(platformGeocoder, dispatcher)
