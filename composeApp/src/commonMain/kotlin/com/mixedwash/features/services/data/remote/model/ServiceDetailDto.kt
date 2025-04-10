package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailDto(
    @SerialName("key") val key: String,
    @SerialName("value") val value: String,
)