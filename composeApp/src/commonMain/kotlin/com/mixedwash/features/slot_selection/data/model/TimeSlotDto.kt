package com.mixedwash.features.slot_selection.data.model

import androidx.compose.runtime.Immutable
import kotlin.random.Random

@Immutable
data class TimeSlotDto(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val startTimeStamp: Long,
    val endTimeStamp: Long,
    val isAvailable: Boolean,
    val unavailableText: String? = null,
    val offersAvailable: List<OfferDto> = emptyList(),
)
