package com.mixedwash.presentation.models

import androidx.compose.foundation.text.KeyboardOptions
import com.mixedwash.domain.validation.FormValidationUseCase

data class FormField(
    val value: String,
    val id: FieldID,
    val inputState: InputState = InputState.Enabled,
    val singleLine: Boolean = true,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val label: String,
    val placeholder: String? = null,
    val errorMessage: String? = null,
    val showRequired: Boolean = false,
    val requiredMessage: String = "Field is required",
    val validationUseCase: FormValidationUseCase? = null,
    val showValidation: Boolean = validationUseCase != null,
    val onValueChange: (String) -> Unit,
) {

    private val supportingText: String? = when {
        inputState is InputState.Disabled -> null
        errorMessage != null && value.isNotBlank() && showValidation -> errorMessage
        value.isBlank() && showRequired -> requiredMessage
        else -> null
    }


    val asFieldState = FieldState(
        value = if (inputState is InputState.Disabled && inputState.disabledMessage != null) inputState.disabledMessage else value,
        supportingText = supportingText,
        readOnly = inputState is InputState.ReadOnly,
        enabled = inputState !is InputState.Disabled,
        isError = supportingText != null,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions
    )

}


enum class FieldID {
    ADDRESS_TITLE, PHONE, EMAIL, ADDRESS_LINE_1, ADDRESS_LINE_2, ADDRESS_LINE_3, PIN_CODE
}


sealed class InputState {
    data object Enabled : InputState()
    data class Disabled(val disabledMessage: String? = null) : InputState()
    data object ReadOnly : InputState()
}