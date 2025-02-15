package com.mixedwash.loki.geocoder.googlemaps.google.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.mixedwash.loki.core.Coordinates
import com.mixedwash.loki.core.InternalLokiApi
import com.mixedwash.loki.core.Place
import com.mixedwash.loki.geocoder.exception.GeocodeException
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.AdministrativeAreaLevel1
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.AdministrativeAreaLevel2
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.AdministrativeAreaLevel3
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.Country
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.Locality
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.Name
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.Neighborhood
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.PostalCode
import com.mixedwash.loki.geocoder.googlemaps.google.internal.AddressComponentType.Thoroughfare

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
            street = response.formattedAddress,
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
        ).takeIf { !it.isEmpty }
    }
}