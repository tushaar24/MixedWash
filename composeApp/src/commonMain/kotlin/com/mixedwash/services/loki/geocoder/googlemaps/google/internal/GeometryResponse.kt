package com.mixedwash.services.loki.geocoder.googlemaps.google.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.mixedwash.services.loki.core.InternalLokiApi

@InternalLokiApi
@Serializable
public data class GeometryResponse(
    @Serializable
    val location: LocationResponse? = null,
)

@InternalLokiApi
@Serializable
public data class LocationResponse(
    @SerialName("lat")
    val lat: Double,

    @SerialName("lng")
    val lng: Double,
)