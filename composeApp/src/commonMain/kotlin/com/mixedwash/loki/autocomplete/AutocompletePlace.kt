package com.mixedwash.loki.autocomplete

import com.mixedwash.loki.autocomplete.googlemaps.web.internal.PlaceAutocompletePrediction


data class AutocompletePlace(
    val placeId: String,
    val address: String
)

fun PlaceAutocompletePrediction.toAutoCompletePlace() : AutocompletePlace {
    return AutocompletePlace(
        placeId = placeId,
        address = description
    )
}