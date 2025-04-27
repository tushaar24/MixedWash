package com.mixedwash.core.feature.auth.domain.error

sealed class UserException {

    class UserNotFoundException(message: String = "User not found") : Exception(message)
}

