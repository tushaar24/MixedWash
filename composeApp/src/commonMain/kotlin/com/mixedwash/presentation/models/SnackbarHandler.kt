package com.mixedwash.presentation.models

import androidx.compose.material3.SnackbarDuration

fun interface SnackbarHandler {
    suspend operator fun invoke(
        snackbarPayload: SnackbarPayload
    )
}
data class SnackbarPayload(
    val message: String,
    val type: SnackBarType = SnackBarType.DEFAULT,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val action: (() -> Unit)? = null,
    val actionText: String? = null
)