@file:Suppress("FunctionName")

package com.mixedwash.services.loki.geocoder.googlemaps

import com.mixedwash.services.loki.core.web.HttpApiEndpoint
import com.mixedwash.services.loki.geocoder.Geocoder
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.GoogleMapsParameters
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.GoogleMapsParametersBuilder
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.googleMapsParameters
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

/**
 * Creates a new [Geocoder] using the Google Maps HTTP API geocoding service.
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param parameters The parameters to use for the geocoder.
 * @param json The JSON implementation to use for serialization.
 * @param client The HTTP client to use for requests.
 * @param dispatcher The coroutine dispatcher to use for requests.
 * @return A new [Geocoder] using the Google Maps HTTP API geocoding service.
 */
public fun Geocoder(
    apiKey: String,
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geocoder = com.mixedwash.services.loki.geocoder.googlemaps.GoogleMapsGeocoder(
    apiKey,
    parameters,
    json,
    client,
    dispatcher
)

/**
 * Creates a new [Geocoder] using the Google Maps HTTP API geocoding service.
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param parameters The parameters to use for the geocoder.
 * @param json The JSON implementation to use for serialization.
 * @param client The HTTP client to use for requests.
 * @param dispatcher The coroutine dispatcher to use for requests.
 * @return A new [Geocoder] using the Google Maps HTTP API geocoding service.
 */
public fun GoogleMapsGeocoder(
    apiKey: String,
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geocoder {
    val platform = GoogleMapsPlatformGeocoder(
        apiKey,
        parameters,
        json,
        client
    )
    return _root_ide_package_.com.mixedwash.services.loki.geocoder.Geocoder(platform, dispatcher)
}

/**
 * Creates a new [Geocoder] using the Google Maps HTTP API geocoding service.
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param json The JSON implementation to use for serialization.
 * @param client The HTTP client to use for requests.
 * @param dispatcher The coroutine dispatcher to use for requests.
 * @param block Customize the [GoogleMapsParameters] to use for the geocoder.
 * @return A new [Geocoder] using the Google Maps HTTP API geocoding service.
 */
public fun GoogleMapsGeocoder(
    apiKey: String,
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: GoogleMapsParametersBuilder.() -> Unit,
): Geocoder = com.mixedwash.services.loki.geocoder.googlemaps.GoogleMapsGeocoder(
    apiKey,
    googleMapsParameters(block),
    json,
    client,
    dispatcher
)

/**
 * Creates a new [Geocoder] using the Google Maps HTTP API geocoding service.
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param parameters The parameters to use for the geocoder.
 * @param json The JSON implementation to use for serialization.
 * @param client The HTTP client to use for requests.
 * @param dispatcher The coroutine dispatcher to use for requests.
 * @return A new [Geocoder] using the Google Maps HTTP API geocoding service.
 */
public fun Geocoder.Companion.googleMaps(
    apiKey: String,
    enableLogging: Boolean = false,
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json = json, enableLogging = enableLogging),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geocoder = com.mixedwash.services.loki.geocoder.googlemaps.GoogleMapsGeocoder(
    apiKey,
    parameters,
    json,
    client,
    dispatcher
)

/**
 * Creates a new [Geocoder] using the Google Maps HTTP API geocoding service.
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param json The JSON implementation to use for serialization.
 * @param client The HTTP client to use for requests.
 * @param dispatcher The coroutine dispatcher to use for requests.
 * @param block Customize the [GoogleMapsParameters] to use for the geocoder.
 * @return A new [Geocoder] using the Google Maps HTTP API geocoding service.
 */
public fun Geocoder.Companion.googleMaps(
    apiKey: String,
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: GoogleMapsParametersBuilder.() -> Unit,
): Geocoder = com.mixedwash.services.loki.geocoder.googlemaps.GoogleMapsGeocoder(
    apiKey,
    json,
    client,
    dispatcher,
    block
)
