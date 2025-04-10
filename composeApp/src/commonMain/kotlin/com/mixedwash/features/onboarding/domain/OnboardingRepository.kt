package com.mixedwash.features.onboarding.domain

import com.mixedwash.features.onboarding.domain.model.OnboardingItem

interface OnboardingRepository {
    suspend fun fetchOnboardingData(): Result<List<OnboardingItem>>
}