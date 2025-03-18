package com.mixedwash.features.slot_selection.domain.model.response

import androidx.compose.runtime.Immutable
import kotlin.random.Random

@Immutable
data class TimeSlot(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val startTimeStamp: Long,
    val endTimeStamp: Long,
    val isAvailable: Boolean,
    val unavailableText: String? = null,
    val offersAvailable: List<Offer> = emptyList(),
) 