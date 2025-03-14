package com.mixedwash.features.slot_selection.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class OfferPresentation(
    val code: String,
    val icon: ImageVector = Icons.Rounded.Star,
    val title: String,
    val subtitle: String,
    val isSelected: Boolean = false,
    val isAvailable: Boolean = true
) 