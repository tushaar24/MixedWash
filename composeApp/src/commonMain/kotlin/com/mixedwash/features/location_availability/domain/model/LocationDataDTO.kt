package com.mixedwash.features.location_availability.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDataDTO(
    @SerialName("service_areas")
    val serviceAreas: List<ServiceAreaDTO> = emptyList(),

    @SerialName("serviceable_pincodes")
    val serviceablePincodes: List<String> = emptyList()
)