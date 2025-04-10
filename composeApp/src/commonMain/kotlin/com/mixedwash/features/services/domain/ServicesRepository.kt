package com.mixedwash.features.services.domain

import com.mixedwash.features.services.data.remote.model.ServiceDto

interface ServicesRepository {
    suspend fun getAllServices(): Result<List<ServiceDto>>

    /**
     * @param id the service id or the item id
     * @return the service or item if found
     */
    suspend fun getServiceById(id: String): Result<ServiceDto>
}