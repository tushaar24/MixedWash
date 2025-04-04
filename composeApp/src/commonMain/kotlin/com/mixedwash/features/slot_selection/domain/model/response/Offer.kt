package com.mixedwash.features.slot_selection.domain.model.response

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class Offer(
    val code: String,
    val icon: ImageVector = Icons.Rounded.Star,
    val title: String,
    val subtitle: String,
    val isSelected: Boolean = false,
    val isAvailable: Boolean = true
) 