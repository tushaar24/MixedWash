package com.mixedwash.core.feature.auth.domain.model

data class User(
    val uid: String,
    val providerId: String?,
    val lastSignInTime: Double?,
    val userMetadata: UserMetadata? = null
)