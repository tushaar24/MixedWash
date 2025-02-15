package com.mixedwash.presentation.models

import androidx.compose.material3.SnackbarDuration

fun interface SnackbarHandler {
    suspend operator fun invoke(
        message: String,
        type: SnackBarType,
        duration: SnackbarDuration?
    )
}