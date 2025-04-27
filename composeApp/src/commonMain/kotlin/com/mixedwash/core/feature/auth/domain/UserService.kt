package com.mixedwash.core.feature.auth.domain

import com.mixedwash.core.domain.models.Result
import com.mixedwash.core.feature.auth.domain.model.AuthState
import com.mixedwash.core.feature.auth.domain.model.User
import com.mixedwash.core.feature.auth.domain.model.UserMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface UserService {
    val authState: Flow<AuthState>

    val userStateFlow: StateFlow<User?>

    val currentUser: User?

    val isSignedIn: Boolean

    suspend fun signOut(): Result<Unit>

    suspend fun updateMetadata(
        update: (UserMetadata) -> UserMetadata
    ): Result<UserMetadata>

    suspend fun deleteMetadata(): Result<Unit>

}