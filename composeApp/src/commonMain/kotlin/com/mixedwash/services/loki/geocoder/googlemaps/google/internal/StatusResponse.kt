package com.mixedwash.services.loki.geocoder.googlemaps.google.internal

import kotlinx.serialization.SerialName
import com.mixedwash.services.loki.core.InternalLokiApi

/**
 * The possible status responses from the Google Maps Geocoding API.
 *
 * @see [Status and Error Codes](https://developers.google.com/maps/documentation/geocoding/requests-reverse-geocoding#reverse-response)
 */
@Suppress("unused")
@InternalLokiApi
public enum class StatusResponse {
    @SerialName("OK")
    Ok,

    @SerialName("ZERO_RESULTS")
    ZeroResults,

    @SerialName("OVER_QUERY_LIMIT")
    OverQueryLimit,

    @SerialName("REQUEST_DENIED")
    RequestDenied,

    @SerialName("INVALID_REQUEST")
    InvalidRequest,

    @SerialName("UNKNOWN_ERROR")
    UnknownError,
}