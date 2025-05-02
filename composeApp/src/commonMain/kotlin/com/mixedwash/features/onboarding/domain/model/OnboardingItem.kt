package com.mixedwash.features.onboarding.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingItem(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("background_color_hex") val backgroundColorHex: String
)