package com.mixedwash.features.location_availability.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationAvailabilityDTO (
    @SerialName("data")
    val data: LocationDataDTO,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("schema_version")
    val schemaVersion: String
)
