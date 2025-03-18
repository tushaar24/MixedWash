package com.mixedwash.features.slot_selection.domain.model.response

import androidx.compose.runtime.Immutable
import kotlin.random.Random

@Immutable
data class DateSlot(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val timeStamp: Long,
    val timeSlots: List<TimeSlot>,
) {
    fun isAvailable(): Boolean = timeSlots.any { it.isAvailable }
} 