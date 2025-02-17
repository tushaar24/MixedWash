package com.mixedwash.services.loki.geocoder.internal

import com.mixedwash.services.loki.geocoder.Geocoder
import com.mixedwash.services.loki.geocoder.GeocoderResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.mixedwash.services.loki.core.Coordinates
import com.mixedwash.services.loki.core.Place
import com.mixedwash.services.loki.core.exception.NotFoundException
import com.mixedwash.services.loki.core.exception.NotSupportedException

internal class DefaultGeocoder(
    override val platformGeocoder: com.mixedwash.services.loki.geocoder.PlatformGeocoder,
    private val dispatcher: CoroutineDispatcher,
) : Geocoder {

    /**
     * @see Geocoder.isAvailable
     */
    override fun isAvailable(): Boolean = platformGeocoder.isAvailable()

    /**
     * @see Geocoder.reverse
     */
    override suspend fun reverse(latitude: Double, longitude: Double): GeocoderResult<Place> {
        return handleResult {
            platformGeocoder.reverse(latitude, longitude)
        }
    }

    /**
     * @see Geocoder.forward
     */
    override suspend fun forward(placeId: String): GeocoderResult<Coordinates> {
        return handleResult {
            platformGeocoder.forward(placeId)
        }
    }

    private suspend fun <T> handleResult(block: suspend () -> List<T>): GeocoderResult<T> {
        try {
            if (!isAvailable()) {
                return GeocoderResult.NotSupported
            }
            val place = withContext(dispatcher) { block() }
            if (place.isEmpty()) {
                return GeocoderResult.NotFound
            }

            return GeocoderResult.Success(place)
        } catch (cause: CancellationException) {
            throw cause
        } catch (cause: Throwable) {
            return when (cause) {
                is NotSupportedException -> GeocoderResult.NotSupported
                is IllegalArgumentException -> GeocoderResult.InvalidCoordinates
                is NotFoundException -> GeocoderResult.NotFound
                else -> GeocoderResult.GeocodeFailed(cause.message ?: "Unknown error")
            }
        }
    }
}