package com.mixedwash.core.feature.auth.domain.model

sealed class AuthState {
    data object Loading : AuthState()
    data object Authenticated : AuthState()
    data object Authenticating : AuthState()
    data object Unauthenticated : AuthState()
}