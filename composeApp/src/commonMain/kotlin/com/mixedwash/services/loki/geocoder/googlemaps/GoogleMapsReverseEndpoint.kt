package com.mixedwash.services.loki.geocoder.googlemaps

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import com.mixedwash.services.loki.core.Coordinates
import com.mixedwash.services.loki.core.InternalLokiApi
import com.mixedwash.services.loki.core.Place
import com.mixedwash.services.loki.geocoder.googlemaps.google.internal.GeocodeResponse
import com.mixedwash.services.loki.geocoder.googlemaps.google.internal.resultsOrThrow
import com.mixedwash.services.loki.geocoder.googlemaps.google.internal.toPlaces
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.GoogleMapsParameters
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.GoogleMapsParametersBuilder
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.googleMapsParameters
import com.mixedwash.services.loki.geocoder.web.ReverseEndpoint

/**
 * A [ReverseEndpoint] that uses the Google Maps Geocoding API.
 *
 * @property apiKey The API key to use for the Google Maps Geocoding API.
 * @property parameters The parameters to use for the Google Maps Geocoding API.
 */
public class GoogleMapsReverseEndpoint(
    private val apiKey: String,
    private val parameters: GoogleMapsParameters = GoogleMapsParameters(),
) : ReverseEndpoint {

    /**
     * Creates a new [GoogleMapsReverseEndpoint] with the given API key.
     *
     * @param apiKey The API key to use for the Google Maps Geocoding API.
     * @param block A block to configure the parameters for the Google Maps Geocoding API.
     */
    public constructor(
        apiKey: String,
        block: GoogleMapsParametersBuilder.() -> Unit,
    ) : this(apiKey, googleMapsParameters(block))

    override fun url(param: Coordinates): String {
        val (latitude, longitude) = param.run { latitude to longitude }
        return GoogleMapsPlatformGeocoder.reverseUrl(latitude, longitude, apiKey, parameters)
    }

    @OptIn(InternalLokiApi::class)
    override suspend fun mapResponse(response: HttpResponse): List<Place> {
        val result = response.body<GeocodeResponse>().resultsOrThrow()
        return result.toPlaces()
    }
}