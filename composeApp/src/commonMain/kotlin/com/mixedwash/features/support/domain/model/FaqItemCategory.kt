package com.mixedwash.features.support.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class FaqItemCategory {
    All, General, Pickup, Processing, Delivery
}