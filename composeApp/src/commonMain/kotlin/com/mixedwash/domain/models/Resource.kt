package com.mixedwash.domain.models

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val error: ErrorType) : Resource<Nothing>()
}