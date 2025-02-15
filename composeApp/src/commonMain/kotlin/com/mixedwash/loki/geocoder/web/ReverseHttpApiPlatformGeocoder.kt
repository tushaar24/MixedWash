package com.mixedwash.loki.geocoder.web

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import com.mixedwash.loki.core.Coordinates
import com.mixedwash.loki.core.InternalLokiApi
import com.mixedwash.loki.core.Place
import com.mixedwash.loki.core.exception.NotSupportedException
import com.mixedwash.loki.core.web.HttpApiEndpoint
import com.mixedwash.loki.core.web.makeRequest

/**
 * A [HttpApiPlatformGeocoder] for reverse geocoding that uses the provided [ReverseEndpoint].
 *
 * @see HttpApiPlatformGeocoder
 * @property endpoint The endpoint to use for reverse geocoding.
 * @property client The [HttpClient] to use for making requests.
 */
public class ReverseHttpApiPlatformGeocoder(
    private val endpoint: ReverseEndpoint,
    private val client: HttpClient,
) : HttpApiPlatformGeocoder {

    override fun isAvailable(): Boolean = true

    override suspend fun forward(address: String): List<Coordinates> {
        throw NotSupportedException()
    }

    @OptIn(InternalLokiApi::class)
    override suspend fun reverse(latitude: Double, longitude: Double): List<Place> {
        val url = endpoint.url(Coordinates(latitude, longitude))
        return client.makeRequest(url, endpoint::mapResponse)
    }
}

/**
 * An endpoint for reverse geocoding.
 *
 * @param url The URL to use for reverse geocoding.
 * @param mapResponse A function to map the response to a list of [Coordinates].
 */
public fun ReverseEndpoint(
    url: (Coordinates) -> String,
    mapResponse: suspend (HttpResponse) -> List<Place>,
): ReverseEndpoint = HttpApiEndpoint(url, mapResponse)