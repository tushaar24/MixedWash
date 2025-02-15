package com.mixedwash.loki.geocoder.googlemaps.google.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.mixedwash.loki.core.InternalLokiApi

@InternalLokiApi
@Serializable
public data class ResultResponse(
    @SerialName("address_components")
    public val addressComponents: List<AddressComponentResponse> = emptyList(),

    @SerialName("formatted_address")
    public val formattedAddress: String? = null,

    @SerialName("geometry")
    public val geometry: GeometryResponse? = null,
)

@InternalLokiApi
@Serializable
public data class AddressComponentResponse(
    @SerialName("long_name")
    val long: String,

    @SerialName("short_name")
    val short: String,

    @SerialName("types")
    val types: List<String> = emptyList(),
)

internal enum class AddressComponentType(val value: String) {
    Name("route"),
    Thoroughfare("route"),
    Neighborhood("neighborhood"),
    Locality("locality"),
    Country("country"),
    AdministrativeAreaLevel1("administrative_area_level_1"),
    AdministrativeAreaLevel2("administrative_area_level_2"),
    AdministrativeAreaLevel3("administrative_area_level_3"),
    PostalCode("postal_code"),
}

@OptIn(InternalLokiApi::class)
internal fun List<AddressComponentResponse>.find(
    component: AddressComponentType,
): AddressComponentResponse? {
    return find { it.types.contains(component.value) }
}