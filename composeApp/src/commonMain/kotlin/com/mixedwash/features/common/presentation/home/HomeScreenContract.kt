package com.mixedwash.features.common.presentation.home

import com.mixedwash.core.presentation.models.SnackbarPayload

sealed interface HomeScreenUiEvent {
    data class ShowSnackbar(val payload: SnackbarPayload) : HomeScreenUiEvent
}