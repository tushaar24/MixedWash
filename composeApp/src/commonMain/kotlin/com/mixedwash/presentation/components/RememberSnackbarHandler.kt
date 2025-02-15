package com.mixedwash.presentation.components

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mixedwash.presentation.models.SnackbarHandler
import com.mixedwash.presentation.models.withSnackbarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSnackbarHandler(
    scaffoldState: BottomSheetScaffoldState
): SnackbarHandler {
    return remember(scaffoldState.snackbarHostState) {
        SnackbarHandler { message, type, duration ->
            scaffoldState.snackbarHostState.showSnackbar(
                message.withSnackbarType(type),
                duration = duration ?: SnackbarDuration.Short,
            )
        }
    }
}
