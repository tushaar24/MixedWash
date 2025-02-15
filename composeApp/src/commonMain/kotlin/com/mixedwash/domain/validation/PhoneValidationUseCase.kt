package com.mixedwash.domain.validation

import com.mixedwash.domain.validation.models.FormValidationResult

object PhoneValidationUseCase : FormValidationUseCase {
    override operator fun invoke(input: String): FormValidationResult {
        val phoneRegex = "^\\d{10}\$".toRegex()
        return if (phoneRegex.matches(input)) {
            FormValidationResult(true, null)
        } else {
            FormValidationResult(false, "Phone number must be 10 digits only")
        }
    }
}