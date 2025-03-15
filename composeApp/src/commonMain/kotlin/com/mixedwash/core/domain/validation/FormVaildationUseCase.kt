package com.mixedwash.core.domain.validation

import com.mixedwash.core.domain.validation.models.FormValidationResult

interface FormValidationUseCase {
    operator fun invoke(input: String): FormValidationResult
}