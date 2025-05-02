package com.mixedwash.core.domain.validation

import com.mixedwash.core.domain.validation.models.FormValidationResult

object NameValidationUseCase : FormValidationUseCase {
    override operator fun invoke(input: String): FormValidationResult {
        return if (input.isEmpty()) {
            FormValidationResult(false, "Name should not be empty.")
        } else {
            FormValidationResult(true, null)
        }
    }
}