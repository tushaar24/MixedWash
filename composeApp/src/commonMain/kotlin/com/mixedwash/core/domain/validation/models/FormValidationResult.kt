package com.mixedwash.core.domain.validation.models

data class FormValidationResult(
    val isValid: Boolean,
    val errorMessage: String?
)