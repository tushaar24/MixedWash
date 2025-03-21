package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GenderDto {
    @SerialName("male") MALE,
    @SerialName("female") FEMALE,
}
