package com.mixedwash.features.services.data.remote

import com.mixedwash.features.services.data.remote.model.ServiceDto
import com.mixedwash.features.services.data.remote.model.ServiceItemDto
import com.mixedwash.features.services.data.remote.model.ServicesResponseDto
import com.mixedwash.features.services.domain.ServicesDataRepository
import com.mixedwash.features.services.domain.error.ServicesException.ServiceItemNotFoundException
import com.mixedwash.features.services.domain.error.ServicesException.ServiceNotFoundException
import com.mixedwash.features.services.domain.error.ServicesException.ServicesCannotBeFetchedException
import kotlinx.serialization.json.Json
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val filePath = "files/mock/services_data.json"

@OptIn(ExperimentalResourceApi::class)
class MockServicesDataRepository : ServicesDataRepository {

    private var servicesResponseDtoCache: ServicesResponseDto? = null

    override suspend fun getAllServices(): Result<ServicesResponseDto> {
        servicesResponseDtoCache?.let {
            return Result.success(it)
        }
        return try {
            val string = Res.readBytes(filePath).decodeToString()
            val json = Json { ignoreUnknownKeys = true }
            val result = json.decodeFromString<ServicesResponseDto>(string)
            servicesResponseDtoCache = result
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(ServicesCannotBeFetchedException("Error trying to fetch services"))
        }

    }

    override suspend fun getServiceById(id: String): Result<ServiceDto> {
        val services = servicesResponseDtoCache?.services ?: getAllServices().getOrNull()?.services
        ?: return Result.failure(Exception())
        services.find { service ->
            service.serviceId == id || service.items?.find { it.itemId == id } != null
        }?.let {
            return Result.success(it)
        } ?: return Result.failure(ServiceNotFoundException(id))
    }

    override suspend fun getServiceItemById(id: String): Result<ServiceItemDto> {
        val services = servicesResponseDtoCache?.services ?: getAllServices().getOrNull()?.services
        ?: return Result.failure(Exception())
        val item = services.firstNotNullOfOrNull { service ->
            service.items?.firstOrNull { item -> item.itemId == id }
        } ?: return Result.failure(ServiceItemNotFoundException(id))
        return Result.success(item)
    }

    override suspend fun getServiceByServiceItemId(itemId: String): Result<ServiceDto> {
        val services = servicesResponseDtoCache?.services ?: getAllServices().getOrNull()?.services
        ?: return Result.failure(Exception())
        val service = services.find { service ->
            service.items?.any { item -> item.itemId == itemId } ?: false
        }
            ?: return Result.failure(ServiceNotFoundException("Service not found for item ID: $itemId"))
        return Result.success(service)
    }
}