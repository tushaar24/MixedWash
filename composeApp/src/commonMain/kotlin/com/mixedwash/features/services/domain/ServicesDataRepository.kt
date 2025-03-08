package com.mixedwash.features.services.domain

import com.mixedwash.features.services.data.remote.model.ServiceResponseDto

interface ServicesDataRepository {
    suspend fun getServices() : Result<ServiceResponseDto>
}