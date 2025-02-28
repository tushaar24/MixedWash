package com.mixedwash.core.domain.models

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: ErrorType) : Result<Nothing>()

    fun getOrNull(): T? {
        return when (this) {
            is Success -> data
            is Error -> null
        }
    }
}