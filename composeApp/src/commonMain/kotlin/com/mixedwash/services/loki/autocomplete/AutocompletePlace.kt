package com.mixedwash.services.loki.autocomplete

import com.mixedwash.services.loki.autocomplete.googlemaps.web.internal.PlaceAutocompletePrediction


data class AutocompletePlace(
    val placeId: String,
    val address: String,
)

fun PlaceAutocompletePrediction.toAutoCompletePlace() : AutocompletePlace {
    return AutocompletePlace(
        placeId = placeId,
        address = terms.joinToString(", ") { it.value },
    )
}