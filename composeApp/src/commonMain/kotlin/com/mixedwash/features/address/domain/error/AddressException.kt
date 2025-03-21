package com.mixedwash.features.address.domain.error

class AddressNotFoundException(message: String = "Address not found") : Exception(message)
class OperationFailedException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

