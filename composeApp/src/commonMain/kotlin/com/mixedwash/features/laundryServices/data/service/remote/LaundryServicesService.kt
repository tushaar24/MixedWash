package com.mixedwash.features.laundryServices.data.service.remote

import com.mixedwash.features.laundryServices.data.entities.ServiceListResponseEntity
import com.mixedwash.features.laundryServices.data.entities.ServiceResponseEntity
import io.ktor.client.HttpClient

class LaundryServicesService  constructor (
    private val ktorClient: HttpClient
) {
    suspend fun fetchAllLaundryServices() : ServiceListResponseEntity {
        return ServiceListResponseEntity(
            listOf(
                ServiceResponseEntity(
                    serviceName = "Wash and Fold",
                    price = 95.00,
                    serviceId = "f3a9c2d0-8b67-4c1e-99e7-2e4a1d6b8c55",
                    unit = "Kg"
                )
            )
        )
    }
}