package com.mixedwash.core.booking.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingTimeSlot(
    @SerialName("id")
    val id: Int,
    @SerialName("start_time_stamp")
    val startTimeStamp: Long,
    @SerialName("end_time_stamp")
    val endTimeStamp: Long,
)
