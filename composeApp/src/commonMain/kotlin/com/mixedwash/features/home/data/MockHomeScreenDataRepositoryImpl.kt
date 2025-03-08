package com.mixedwash.features.home.data

import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.home.data.models.HomeScreenDataDto
import com.mixedwash.features.home.domain.HomeScreenDataRepository
import kotlinx.serialization.json.Json
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val filePath = "files/mock/home_screen_data.json"

class MockHomeScreenDataRepositoryImpl : HomeScreenDataRepository {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun fetchData(): Result<HomeScreenDataDto> {
        val bytes = Res.readBytes(filePath)
        val string = bytes.decodeToString()
        return try {

            Result.Success(Json.decodeFromString<HomeScreenDataDto>(string))
        } catch (e: Exception) {
            Result.Error(ErrorType.Unknown(e.message?:"Error Fetching Mock Data"))
        }
    }

}