package com.mixedwash.services.loki.geocoder.googlemaps

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import com.mixedwash.services.loki.core.web.HttpApiEndpoint
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.GoogleMapsParameters
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.GoogleMapsParametersBuilder
import com.mixedwash.services.loki.geocoder.googlemaps.parameter.googleMapsParameters
import com.mixedwash.services.loki.geocoder.web.HttpApiPlatformGeocoder

/**
 * Defines a [HttpApiPlatformGeocoder] that uses the Google Maps Geocoding API.
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding)
 * for more information.
 */
public interface GoogleMapsPlatformGeocoder : HttpApiPlatformGeocoder {

    public companion object {

        private const val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json"

        private fun createUrl(
            target: String,
            apiKey: String,
            params: GoogleMapsParameters,
        ): String = "$BASE_URL?$target&${params.encode()}&key=$apiKey"

        internal fun forwardUrl(placeId: String, apiKey: String, params: GoogleMapsParameters) =
            createUrl(target = "place_id=$placeId", apiKey = apiKey, params = params)

        internal fun reverseUrl(
            latitude: Double,
            longitude: Double,
            apiKey: String,
            params: GoogleMapsParameters,
        ) = createUrl(target = "latlng=$latitude,$longitude", apiKey = apiKey, params = params)
    }
}

/**
 * Creates a [GoogleMapsPlatformGeocoder] to be used with the [Geocoder].
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param parameters The parameters to use for the geocoding requests.
 * @param json The [Json] instance to use for serialization and deserialization.
 * @param client The [HttpClient] to use for making requests.
 * @return A [GoogleMapsPlatformGeocoder] instance.
 */
public fun GoogleMapsPlatformGeocoder(
    apiKey: String,
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
): GoogleMapsPlatformGeocoder {
    val delegate = HttpApiPlatformGeocoder(
        forwardEndpoint = GoogleMapsForwardEndpoint(apiKey, parameters),
        reverseEndpoint = GoogleMapsReverseEndpoint(apiKey, parameters),
        json = json,
        client = client,
    )
    return object : HttpApiPlatformGeocoder by delegate, GoogleMapsPlatformGeocoder {}
}

/**
 * Creates a [GoogleMapsPlatformGeocoder] to be used with the [Geocoder].
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param json The [Json] instance to use for serialization and deserialization.
 * @param client The [HttpClient] to use for making requests.
 * @param block A lambda that configures the [GoogleMapsParameters] to use for the geocoding requests.
 * @return A [GoogleMapsPlatformGeocoder] instance.
 */
public fun GoogleMapsPlatformGeocoder(
    apiKey: String,
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    block: GoogleMapsParametersBuilder.() -> Unit,
): GoogleMapsPlatformGeocoder =
    GoogleMapsPlatformGeocoder(apiKey, googleMapsParameters(block), json, client)

/**
 * Creates a [GoogleMapsPlatformGeocoder] to be used with the [Geocoder].
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param parameters The parameters to use for the geocoding requests.
 * @param json The [Json] instance to use for serialization and deserialization.
 * @param client The [HttpClient] to use for making requests.
 * @return A [GoogleMapsPlatformGeocoder] instance.
 */
public fun com.mixedwash.services.loki.geocoder.PlatformGeocoder.Companion.googleMaps(
    apiKey: String,
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
): GoogleMapsPlatformGeocoder =
    GoogleMapsPlatformGeocoder(apiKey, parameters, json, client)

/**
 * Creates a [GoogleMapsPlatformGeocoder] to be used with the [Geocoder].
 *
 * See [Google Maps](https://developers.google.com/maps/documentation/geocoding) for more information.
 *
 * @param apiKey The Google Maps API key.
 * @param json The [Json] instance to use for serialization and deserialization.
 * @param client The [HttpClient] to use for making requests.
 * @param block A lambda that configures the [GoogleMapsParameters] to use for the geocoding requests.
 * @return A [GoogleMapsPlatformGeocoder] instance.
 */
public fun com.mixedwash.services.loki.geocoder.PlatformGeocoder.Companion.googleMaps(
    apiKey: String,
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    block: GoogleMapsParametersBuilder.() -> Unit,
): GoogleMapsPlatformGeocoder =
    GoogleMapsPlatformGeocoder(apiKey, googleMapsParameters(block), json, client)
