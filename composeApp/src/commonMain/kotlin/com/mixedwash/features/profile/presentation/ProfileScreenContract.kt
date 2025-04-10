package com.mixedwash.features.profile.presentation

import com.mixedwash.core.presentation.navigation.Route

data class ProfileScreenState(
    val sections: List<ProfileSection>,
    val imageUrl: String? = null,
    val name: String?,
    val email: String? = null,
    val phone: String?,
    val appName: String,
    val appVersion: String,
)

sealed interface ProfileScreenEvent {
    data object OnLogout : ProfileScreenEvent
    data object OnEditProfile : ProfileScreenEvent
}

sealed interface ProfileScreenUiEvent {
    data class Navigate(val route: Route) : ProfileScreenUiEvent
}