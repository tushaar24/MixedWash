package com.mixedwash.core.domain.error

class UnauthorizedRequestException(message: String = "User is not authorized to make this request") : Exception(message)