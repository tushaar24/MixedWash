package com.mixedwash.loki.autocomplete.googlemaps.web.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

// Root response from the autocomplete API
@Serializable
data class AutocompleteApiResult(
    val predictions: List<PlaceAutocompletePrediction>,
    val status: ServiceStatus,
    @SerialName("error_message")
    val errorMessage: String? = null,
    @SerialName("info_messages")
    val infoMessages: List<String>? = null
)

@Serializable
enum class ServiceStatus(val message: String) {
    @SerialName("OK")
    OK("API request was successful"),

    @SerialName("ZERO_RESULTS")
    ZERO_RESULTS("Search was successful but returned no results"),

    @SerialName("INVALID_REQUEST")
    INVALID_REQUEST("API request was malformed due to a missing input parameter"),

    @SerialName("OVER_QUERY_LIMIT")
    OVER_QUERY_LIMIT("Quota exceeded due to API limits or billing issues"),

    @SerialName("REQUEST_DENIED")
    REQUEST_DENIED("Request was denied, possibly due to an invalid or missing API key"),

    @SerialName("UNKNOWN_ERROR")
    UNKNOWN_ERROR("An unknown error occurred"),

    @JsonNames("UNKNOWN", "UNDEFINED", "NEW_ERROR")
    UNKNOWN("An unrecognized error occurred");
}



// A single prediction in the autocomplete response
@Serializable
data class PlaceAutocompletePrediction(
    val description: String,
    @SerialName("matched_substrings")
    val matchedSubstrings: List<PlaceAutocompleteMatchedSubstring>,
    @SerialName("place_id")
    val placeId: String,
    val reference: String,
    @SerialName("structured_formatting")
    val structuredFormatting: PlaceAutocompleteStructuredFormatting,
    val terms: List<PlaceAutocompleteTerm>,
    val types: List<String>? = null
)

// Details on which parts of the description match the input.
@Serializable
data class PlaceAutocompleteMatchedSubstring(
    val length: Int,
    val offset: Int
)

// Pre-formatted text for the prediction that you can display directly.
@Serializable
data class PlaceAutocompleteStructuredFormatting(
    @SerialName("main_text")
    val mainText: String,
    @SerialName("main_text_matched_substrings")
    val mainTextMatchedSubstrings: List<PlaceAutocompleteMatchedSubstring>,
    @SerialName("secondary_text")
    val secondaryText: String? = null,
    @SerialName("secondary_text_matched_substrings")
    val secondaryTextMatchedSubstrings: List<PlaceAutocompleteMatchedSubstring>? = null
)

// A term represents a component of the description.
@Serializable
data class PlaceAutocompleteTerm(
    val offset: Int,
    val value: String
)

// You can define a helper to handle the response status
fun AutocompleteApiResult.resultOrThrow(): List<PlaceAutocompletePrediction> {
    if (status != ServiceStatus.OK) {
        throw Exception("Autocomplete API error: status=${status.name} : ${status.message}, message=${errorMessage ?: "unknown error"}")
    }
    return predictions
}
