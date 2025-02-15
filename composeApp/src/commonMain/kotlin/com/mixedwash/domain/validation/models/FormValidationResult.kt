package com.mixedwash.domain.validation.models

data class FormValidationResult(
    val isValid: Boolean,
    val errorMessage: String?
)