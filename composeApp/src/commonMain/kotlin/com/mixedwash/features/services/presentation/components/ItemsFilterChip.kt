package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ItemsFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor =
        if (isSelected) BrandTheme.colors.gray.lighter else BrandTheme.colors.gray.dark
    val containerColor by animateColorAsState(if (isSelected) BrandTheme.colors.gray.darker else Color.Transparent)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable(onClick = onClick)
            .widthIn(min = 72.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            color = contentColor,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}