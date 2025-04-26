package com.mixedwash.features.home.data

import com.mixedwash.features.home.data.models.HomeScreenDataDto
import com.mixedwash.features.home.domain.HomeScreenDataRepository
import kotlinx.serialization.json.Json
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val filePath = "files/mock/home_screen_data.json"

class MockHomeScreenDataRepositoryImpl : HomeScreenDataRepository {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun fetchData(): Result<HomeScreenDataDto> = runCatching {
        val bytes = Res.readBytes(filePath)
        val string = bytes.decodeToString()
        Json.decodeFromString<HomeScreenDataDto>(string)
    }
}