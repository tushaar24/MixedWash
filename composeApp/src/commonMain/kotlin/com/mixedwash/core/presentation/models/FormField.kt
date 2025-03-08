package com.mixedwash.core.presentation.models

import androidx.compose.foundation.text.KeyboardOptions
import com.mixedwash.core.domain.validation.FormValidationUseCase

data class FormField(
    val value: String,
    val id: com.mixedwash.core.presentation.models.FieldID,
    val inputState: com.mixedwash.core.presentation.models.InputState = _root_ide_package_.com.mixedwash.core.presentation.models.InputState.Enabled,
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
        inputState is com.mixedwash.core.presentation.models.InputState.Disabled -> null
        errorMessage != null && value.isNotBlank() && showValidation -> errorMessage
        value.isBlank() && showRequired -> requiredMessage
        else -> null
    }


    val asFieldState = _root_ide_package_.com.mixedwash.core.presentation.models.FieldState(
        value = if (inputState is _root_ide_package_.com.mixedwash.core.presentation.models.InputState.Disabled && inputState.disabledMessage != null) inputState.disabledMessage else value,
        supportingText = supportingText,
        readOnly = inputState is _root_ide_package_.com.mixedwash.core.presentation.models.InputState.ReadOnly,
        enabled = inputState !is _root_ide_package_.com.mixedwash.core.presentation.models.InputState.Disabled,
        isError = supportingText != null,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions
    )

}

data class FieldState(
    val value: String,
    val onValueChange: (String) -> Unit,
    val label: String,
    val supportingText: String? = null,
    val placeholder: String? = null,
    val singleLine: Boolean,
    val readOnly: Boolean,
    val keyboardOptions: KeyboardOptions,
    val enabled: Boolean,
    val isError: Boolean
)


enum class FieldID {
    ADDRESS_TITLE, PHONE, EMAIL, ADDRESS_LINE_1, ADDRESS_LINE_2, ADDRESS_LINE_3, PIN_CODE, NAME
}


sealed class InputState {
    data object Enabled : _root_ide_package_.com.mixedwash.core.presentation.models.InputState()
    data class Disabled(val disabledMessage: String? = null) : _root_ide_package_.com.mixedwash.core.presentation.models.InputState()
    data object ReadOnly : _root_ide_package_.com.mixedwash.core.presentation.models.InputState()
}