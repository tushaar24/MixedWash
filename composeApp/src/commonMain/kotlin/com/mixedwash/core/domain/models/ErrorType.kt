package com.mixedwash.core.domain.models

sealed class ErrorType(open val message: String) {
    data class Network(override val message: String) : ErrorType(message) {
        override fun toString(): String = "Network Error: $message"
    }

    data class Api(val code: Int, override val message: String) : ErrorType(message) {
        override fun toString(): String = "API Error $code: $message"
    }

    data class Unknown(override val message: String) : ErrorType(message) {
        override fun toString(): String = "Unknown Error: $message"
    }

    data class NoInternet(override val message: String = "No Internet Connection") : ErrorType(message = "No Internet Connection") {
        override fun toString(): String = "No Internet Connection. Please check your connection and try again."
    }

    data class NotFound(override val message: String)  : ErrorType(message) {
        override fun toString(): String = "Not Found: $message"
    }
}
