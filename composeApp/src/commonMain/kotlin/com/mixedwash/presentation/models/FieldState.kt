package com.mixedwash.presentation.models

import androidx.compose.foundation.text.KeyboardOptions

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