package com.mixedwash.core.domain.validation

import com.mixedwash.core.domain.validation.models.FormValidationResult

object EmailValidationUseCase : FormValidationUseCase {
    override operator fun invoke(input: String): FormValidationResult {
        val emailRegex = "^[\\w.-]+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$".toRegex()
        return if (emailRegex.matches(input)) {
            FormValidationResult(true, null)
        } else {
            FormValidationResult(false, "Invalid email address.")
        }
    }
}