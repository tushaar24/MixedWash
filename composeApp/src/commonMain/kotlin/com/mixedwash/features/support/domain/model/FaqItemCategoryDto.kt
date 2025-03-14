package com.mixedwash.features.support.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FaqItemCategoryDto {
    @SerialName("all") All,
    @SerialName("general") General,
    @SerialName("pickup") Pickup,
    @SerialName("delivery") Delivery,
    @SerialName("processing") Processing,
}