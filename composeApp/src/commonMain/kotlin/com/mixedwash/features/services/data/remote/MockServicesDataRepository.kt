package com.mixedwash.features.services.data.remote

import com.mixedwash.features.services.data.remote.model.ServiceResponseDto
import com.mixedwash.features.services.domain.ServicesDataRepository
import kotlinx.serialization.json.Json
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val filePath = "files/mock/services_data.json"

class MockServicesDataRepository : ServicesDataRepository  {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun getServices(): Result<ServiceResponseDto> {
        val bytes = Res.readBytes(filePath)
        val string = bytes.decodeToString()
        return try {
//            throw Exception("Hardcoded Exception")
            val json = Json { ignoreUnknownKeys = false }
            Result.success(json.decodeFromString<ServiceResponseDto>(string))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

