package com.mixedwash.core.orders.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Driver info model:
@Serializable
data class DriverInfo(
    @SerialName("driver_id")
    val driverId: String,
    @SerialName("driver_name")
    val driverName: String,
    @SerialName("contact_number")
    val contactNumber: String
)