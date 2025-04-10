package com.mixedwash.features.slot_selection.data.model

import androidx.compose.runtime.Immutable
import kotlin.random.Random


@Immutable
data class DateSlotDto(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val timeStamp: Long,
    val timeSlots: List<TimeSlotDto>,
) {
    fun isAvailable(): Boolean = timeSlots.any { it.isAvailable }

}
