package com.mixedwash.features.laundryServices.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ServiceListResponseEntity(
    @SerialName("service_list")
    val laundryServiceList: List<ServiceResponseEntity>
)
