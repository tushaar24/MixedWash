package com.mixedwash.features.getting_started

import com.mixedwash.Route
import com.mixedwash.features.getting_started.model.GettingStartedItem

data class GettingStartedScreenState(
    val items: List<GettingStartedItem>,
    val currentIndex: Int,
) {
    val currentItem = items[currentIndex]
}

sealed interface GettingStartedScreenEvent {
    data object OnNext : GettingStartedScreenEvent
    data object OnNavigateToHelpCenter : GettingStartedScreenEvent
    data object OnExplore : GettingStartedScreenEvent
}

sealed interface GettingStartedScreenUiEvent {
    data class Navigate(val route: Route) : GettingStartedScreenUiEvent
}