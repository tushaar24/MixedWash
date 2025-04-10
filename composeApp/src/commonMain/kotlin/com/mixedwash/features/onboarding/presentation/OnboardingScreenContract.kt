package com.mixedwash.features.onboarding.presentation

import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.onboarding.domain.model.OnboardingItem

data class OnboardingScreenState(
    val items: List<OnboardingItem>,
    val currentIndex: Int,
)

sealed interface OnboardingScreenEvent {
    data object OnNavigateToHelpCenter : OnboardingScreenEvent
    data object OnExplore : OnboardingScreenEvent
    data object OnSkip : OnboardingScreenEvent
}

sealed interface OnboardingScreenUiEvent {
    data class Navigate(val route: Route) : OnboardingScreenUiEvent
    data object GoBack : OnboardingScreenUiEvent
}