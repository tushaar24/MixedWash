package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponseDto (
    @SerialName("services") val services: List<ServiceDto>,
    @SerialName("schema_version") val schemaVersion: String
)
