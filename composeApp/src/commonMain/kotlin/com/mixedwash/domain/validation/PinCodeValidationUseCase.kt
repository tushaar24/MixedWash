package com.mixedwash.domain.validation

import com.mixedwash.domain.validation.models.FormValidationResult

object PinCodeValidationUseCase : FormValidationUseCase {
    override operator fun invoke(input: String): FormValidationResult {
        val pinCodeRegex = "^\\d{6}\$".toRegex()
        return if (pinCodeRegex.matches(input)) {
            FormValidationResult(true, null)
        } else {
            FormValidationResult(false, "Pin code should be 6 digits.")
        }
    }
}