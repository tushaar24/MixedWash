package com.mixedwash.core.orders.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// An event log to record state transitions:
@Serializable
data class StateEvent(
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("state")
    val state: BookingState,
    @SerialName("note")
    val note: String? = null
)
