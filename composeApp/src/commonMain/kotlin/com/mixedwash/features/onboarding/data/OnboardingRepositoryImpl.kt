package com.mixedwash.features.onboarding.data

import com.mixedwash.features.onboarding.domain.OnboardingRepository
import com.mixedwash.features.onboarding.domain.model.OnboardingItem
import kotlinx.serialization.json.Json
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

class OnboardingRepositoryImpl : OnboardingRepository {
    private val filePath = "files/mock/onboarding_data.json"

    @OptIn(ExperimentalResourceApi::class)
    override suspend fun fetchOnboardingData(): Result<List<OnboardingItem>> {
        val bytes = Res.readBytes(filePath)
        val jsonString = bytes.decodeToString()
        val json = Json { ignoreUnknownKeys = true }
        return Result.success(json.decodeFromString(jsonString))
    }
}

