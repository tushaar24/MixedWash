package com.mixedwash.core.presentation.components

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.models.withSnackbarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSnackbarHandler(
    scaffoldState: BottomSheetScaffoldState
): SnackbarHandler {
    return remember(scaffoldState.snackbarHostState) {
        SnackbarHandler { payload ->
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = payload.message.withSnackbarType(payload.type),
                duration = payload.duration ?: SnackbarDuration.Short,
                actionLabel = payload.actionText,
            )
            if (payload.actionText == null) return@SnackbarHandler
            when (result) {
                SnackbarResult.Dismissed -> Unit
                SnackbarResult.ActionPerformed -> payload.action?.invoke()
            }
        }
    }
}
