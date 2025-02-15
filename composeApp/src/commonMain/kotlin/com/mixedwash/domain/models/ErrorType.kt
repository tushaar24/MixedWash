package com.mixedwash.domain.models

sealed class ErrorType {
    data class Network(val message: String) : ErrorType() {
        override fun toString(): String = "Network Error: $message"
    }

    data class Api(val code: Int, val message: String) : ErrorType() {
        override fun toString(): String = "API Error $code: $message"
    }

    data class Unknown(val message: String) : ErrorType() {
        override fun toString(): String = "Unknown Error: $message"
    }

    data object NoInternet : ErrorType() {
        override fun toString(): String = "No Internet Connection. Please check your connection and try again."
    }
}
