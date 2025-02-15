package com.mixedwash.domain.validation

import com.mixedwash.domain.validation.models.FormValidationResult

interface FormValidationUseCase {
    operator fun invoke(input: String): FormValidationResult
}