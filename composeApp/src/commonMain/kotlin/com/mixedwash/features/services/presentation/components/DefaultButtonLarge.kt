package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultButtonLarge(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(if (enabled) BrandTheme.colors.gray.darker else BrandTheme.colors.gray.normalDark)
    Row(
        modifier = modifier
            .height(52.dp)
            .clip(BrandTheme.shapes.button)
            .background(containerColor)
            .clickable(onClick = onClick, enabled = enabled)
            .padding(horizontal = 28.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = BrandTheme.colors.gray.light)
    }
}