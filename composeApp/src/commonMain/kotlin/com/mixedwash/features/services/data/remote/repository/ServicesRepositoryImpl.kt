package com.mixedwash.features.services.data.remote.repository

import com.mixedwash.features.services.data.remote.model.ServiceDto
import com.mixedwash.features.services.domain.ServicesDataRepository
import com.mixedwash.features.services.domain.ServicesRepository

class ServicesRepositoryImpl(private val servicesDataRepository: ServicesDataRepository) :
    ServicesRepository {
    override suspend fun getAllServices(): Result<List<ServiceDto>> {
        val serviceResponse =
            servicesDataRepository.getServices().getOrNull() ?: return Result.failure(Exception())
        return Result.success(serviceResponse.services)
    }

    override suspend fun getServiceById(id: String): Result<ServiceDto> {
        val services = getAllServices().getOrNull() ?: return Result.failure(Exception())
        services.find { service ->
            service.serviceId == id || service.items?.find { it.itemId == id } != null
        }?.let {
            return Result.success(it)
        } ?: return Result.failure(Exception("service not found"))
    }
}
