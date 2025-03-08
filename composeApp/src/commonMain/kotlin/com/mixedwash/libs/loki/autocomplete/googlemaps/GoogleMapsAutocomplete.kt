@file:Suppress("FunctionName")

package com.mixedwash.libs.loki.autocomplete.googlemaps

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import com.mixedwash.libs.loki.autocomplete.Autocomplete
import com.mixedwash.libs.loki.autocomplete.AutocompleteOptions
import com.mixedwash.libs.loki.autocomplete.AutocompletePlace
import com.mixedwash.libs.loki.autocomplete.googlemaps.web.GoogleMapsAutocompleteService
import com.mixedwash.libs.loki.core.web.HttpApiEndpoint
import com.mixedwash.libs.loki.geocoder.googlemaps.parameter.GoogleMapsParameters
import com.mixedwash.libs.loki.geocoder.googlemaps.parameter.GoogleMapsParametersBuilder
import com.mixedwash.libs.loki.geocoder.googlemaps.parameter.googleMapsParameters

/**
 * Creates a new [Autocomplete] instance that uses the Google Maps Geocoding API.
 *
 * @param apiKey The Google Maps API key.
 * @param options The autocomplete options.
 * @param parameters The Google Maps geocoder parameters.
 * @param json The JSON serializer.
 * @param client The HTTP client to make the requests with.
 * @param dispatcher The coroutine dispatcher.
 * @return A new [Autocomplete] instance.
 */
public fun Autocomplete(
    apiKey: String,
    options: AutocompleteOptions = AutocompleteOptions(),
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Autocomplete<AutocompletePlace> {
    return GoogleMapsAutocomplete(apiKey, options, parameters, json, client, dispatcher)
}

/**
 * Creates a new [Autocomplete] instance that uses the Google Maps Geocoding API.
 *
 * @param apiKey The Google Maps API key.
 * @param options The autocomplete options.
 * @param json The JSON serializer.
 * @param client The HTTP client to make the requests with.
 * @param dispatcher The coroutine dispatcher.
 * @param block The builder block to configure the Google Maps geocoder parameters.
 * @return A new [Autocomplete] instance.
 */
public fun Autocomplete(
    apiKey: String,
    options: AutocompleteOptions = AutocompleteOptions(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: GoogleMapsParametersBuilder.() -> Unit,
): Autocomplete<AutocompletePlace> {
    return GoogleMapsAutocomplete(apiKey, options, json, client, dispatcher, block)
}

/**
 * Creates a new [Autocomplete] instance that uses the Google Maps Geocoding API.
 *
 * @param apiKey The Google Maps API key.
 * @param options The autocomplete options.
 * @param parameters The Google Maps geocoder parameters.
 * @param json The JSON serializer.
 * @param client The HTTP client to make the requests with.
 * @param dispatcher The coroutine dispatcher.
 * @return A new [Autocomplete] instance.
 */
public fun GoogleMapsAutocomplete(
    apiKey: String,
    options: AutocompleteOptions = AutocompleteOptions(),
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Autocomplete<AutocompletePlace> {
    val service = GoogleMapsAutocompleteService(apiKey, json, client)
    return Autocomplete(service, options, dispatcher)
}

/**
 * Creates a new [Autocomplete] instance that uses the Google Maps Geocoding API.
 *
 * @param apiKey The Google Maps API key.
 * @param options The autocomplete options.
 * @param json The JSON serializer.
 * @param client The HTTP client to make the requests with.
 * @param dispatcher The coroutine dispatcher.
 * @param block The builder block to configure the Google Maps geocoder parameters.
 * @return A new [Autocomplete] instance.
 */
public fun GoogleMapsAutocomplete(
    apiKey: String,
    options: AutocompleteOptions = AutocompleteOptions(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: GoogleMapsParametersBuilder.() -> Unit,
): Autocomplete<AutocompletePlace> = GoogleMapsAutocomplete(
    apiKey = apiKey,
    options = options,
    parameters = googleMapsParameters(block),
    json = json,
    client = client,
    dispatcher = dispatcher,
)

/**
 * Creates a new [Autocomplete] instance that uses the Google Maps Geocoding API.
 *
 * @param apiKey The Google Maps API key.
 * @param options The autocomplete options.
 * @param parameters The Google Maps geocoder parameters.
 * @param json The JSON serializer.
 * @param client The HTTP client to make the requests with.
 * @param dispatcher The coroutine dispatcher.
 * @return A new [Autocomplete] instance.
 */
public fun Autocomplete.Companion.googleMaps(
    apiKey: String,
    enableLogging: Boolean = false,
    options: AutocompleteOptions = AutocompleteOptions(),
    parameters: GoogleMapsParameters = GoogleMapsParameters(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json = json, enableLogging = enableLogging),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Autocomplete<AutocompletePlace> {
    return GoogleMapsAutocomplete(apiKey, options, parameters, json, client, dispatcher)
}

/**
 * Creates a new [Autocomplete] instance that uses the Google Maps Geocoding API.
 *
 * @param apiKey The Google Maps API key.
 * @param options The autocomplete options.
 * @param json The JSON serializer.
 * @param client The HTTP client to make the requests with.
 * @param dispatcher The coroutine dispatcher.
 * @param block The builder block to configure the Google Maps geocoder parameters.
 * @return A new [Autocomplete] instance.
 */
public fun Autocomplete.Companion.googleMaps(
    apiKey: String,
    options: AutocompleteOptions = AutocompleteOptions(),
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: GoogleMapsParametersBuilder.() -> Unit,
): Autocomplete<AutocompletePlace> {
    return GoogleMapsAutocomplete(apiKey, options, json, client, dispatcher, block)
}