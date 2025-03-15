package com.mixedwash.features.services.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun ItemsFilterChip(
    leadingIcon: DrawableResource,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {

    val contentColor =
        if (isSelected) BrandTheme.colors.gray.lighter else BrandTheme.colors.gray.dark
    val borderColor = if (isSelected) Color.Transparent else BrandTheme.colors.gray.dark
    val containerColor = if (isSelected) BrandTheme.colors.gray.darker else Color.Transparent
    Row(
        modifier = Modifier
            .clip(BrandTheme.shapes.chip)
            .background(containerColor)
            .border(color = borderColor, width = 0.5.dp, shape = BrandTheme.shapes.chip)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = vectorResource(leadingIcon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = contentColor
        )
        Text(text, color = contentColor, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    }
}