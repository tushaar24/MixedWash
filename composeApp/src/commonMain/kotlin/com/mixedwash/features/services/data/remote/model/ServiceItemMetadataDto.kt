package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceItemMetadataDto(
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("gender") val gender: GenderDto? = null,
)
