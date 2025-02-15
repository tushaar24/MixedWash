package com.mixedwash.loki.geocoder.web


import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodeURLParameter
import com.mixedwash.loki.core.Coordinates
import com.mixedwash.loki.core.InternalLokiApi
import com.mixedwash.loki.core.Place
import com.mixedwash.loki.core.exception.NotSupportedException
import com.mixedwash.loki.core.web.HttpApiEndpoint
import com.mixedwash.loki.core.web.makeRequest

/**
 * A [HttpApiPlatformGeocoder] for forward geocoding that uses the provided [ForwardEndpoint].
 *
 * @see HttpApiPlatformGeocoder
 * @property endpoint The endpoint to use for forward geocoding.
 * @property client The [HttpClient] to use for making requests.
 */
@OptIn(InternalLokiApi::class)
public class ForwardHttpApiPlatformGeocoder(
    private val endpoint: ForwardEndpoint,
    private val client: HttpClient,
) : HttpApiPlatformGeocoder {

    override fun isAvailable(): Boolean = true

    override suspend fun forward(address: String): List<Coordinates> {
        val url = endpoint.url(address.encodeURLParameter())
        return client.makeRequest(url, endpoint::mapResponse)
    }

    override suspend fun reverse(latitude: Double, longitude: Double): List<Place> {
        throw NotSupportedException()
    }
}

/**
 * An endpoint for forward geocoding.
 *
 * @param url The URL to use for forward geocoding.
 * @param mapResponse A function to map the response to a list of [Coordinates].
 */
public fun ForwardEndpoint(
    url: (String) -> String,
    mapResponse: suspend (HttpResponse) -> List<Coordinates>,
): ForwardEndpoint = HttpApiEndpoint(url, mapResponse)