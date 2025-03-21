package com.mixedwash.core.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data class NavRouteArgument(
    val route: Route
)