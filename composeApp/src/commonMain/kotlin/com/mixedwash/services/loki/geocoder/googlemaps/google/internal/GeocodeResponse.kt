package com.mixedwash.services.loki.geocoder.googlemaps.google.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.mixedwash.services.loki.core.Coordinates
import com.mixedwash.services.loki.core.InternalLokiApi
import com.mixedwash.services.loki.core.Place
import com.mixedwash.services.loki.geocoder.exception.GeocodeException
import com.mixedwash.services.loki.geocoder.googlemaps.google.internal.AddressComponentType.*

@InternalLokiApi
@Serializable
public data class GeocodeResponse(
    @SerialName("results")
    public val results: List<ResultResponse> = emptyList(),

    @SerialName("status")
    public val status: StatusResponse,

    @SerialName("error_message")
    public val errorMessage: String? = null,
)

@InternalLokiApi
public fun GeocodeResponse.resultsOrThrow(): List<ResultResponse> = when (status) {
    StatusResponse.Ok,
    StatusResponse.ZeroResults,
        -> results

    else -> throw GeocodeException("[$status] $errorMessage")
}

@OptIn(InternalLokiApi::class)
internal fun List<ResultResponse>.toCoordinates(): List<Coordinates> {
    return mapNotNull { response ->
        val location = response.geometry?.location ?: return@mapNotNull null
        Coordinates(location.lat, location.lng)
    }
}

@InternalLokiApi
public fun List<ResultResponse>.toPlaces(): List<Place> {
    return mapNotNull { response ->
        val components = response.addressComponents
        val country = components.find(Country)
        val coordinates = response.geometry?.location?.run { Coordinates(lat, lng) }
            ?: return@mapNotNull null

        Place(
            coordinates = coordinates,
            name = components.find(Name)?.long,
            street = components.find(StreetNumber)?.long,
            route = components.find(Route)?.long,
            neighbourhood = components.find(Neighbourhood)?.long,
            political = components.find(Political)?.long,
            isoCountryCode = country?.short,
            country = country?.long,
            postalCode = components.find(PostalCode)?.long,
            administrativeArea = components.find(AdministrativeAreaLevel1)?.long,
            subAdministrativeArea = components.find(AdministrativeAreaLevel2)?.long,
            locality = components.find(Locality)?.long
                ?: components.find(AdministrativeAreaLevel3)?.long,
            subLocality = components.find(Neighborhood)?.long,
            thoroughfare = components.find(Thoroughfare)?.long,
            subThoroughfare = null,
            googlePlaceId = response.placeId,
            formattedAddress = response.formattedAddress,
        ).takeIf { !it.isEmpty }
    }
}