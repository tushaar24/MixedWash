package com.mixedwash.features.getting_started

import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.getting_started.model.OnboardingItem

data class OnboardingScreenState(
    val items: List<OnboardingItem>,
    val currentIndex: Int,
)

sealed interface OnboardingScreenEvent {
    data object OnNavigateToHelpCenter : OnboardingScreenEvent
    data object OnExplore : OnboardingScreenEvent
}

sealed interface OnboardingScreenUiEvent {
    data class Navigate(val route: Route) : OnboardingScreenUiEvent
}