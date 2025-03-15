package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mixedwash.core.domain.util.capitalize
import com.mixedwash.ui.theme.MixedWashTheme

@Composable
fun DateSlotButton(
    modifier: Modifier = Modifier,
    day: String,
    date: String,
    selected: Boolean = false,
    disabled: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = if (disabled || !selected) {
        Color.Transparent
    } else {
        BrandTheme.colors.gray.dark
    }
    val contentColor = if (disabled) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        BrandTheme.colors.gray.dark
    } else {
        BrandTheme.colors.gray.lighter
    }

    val clickable = !(disabled || selected)
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .requiredWidth(width = 46.dp)
            .requiredHeight(height = 58.dp)
            .clip(shape = BrandTheme.shapes.chip)
            .clickable(enabled = clickable, onClick = onClick)
            .background(color = containerColor)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = date,
            color = contentColor,
            textAlign = TextAlign.Center,
            style = BrandTheme.typography.largeButton.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = day.capitalize(),
            color = contentColor,
            textAlign = TextAlign.Center,
            style = BrandTheme.typography.tinyButton,
            modifier = Modifier.fillMaxWidth()
        )
    }
}



//@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDateSlot() {
    MixedWashTheme {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                8.dp, alignment = Alignment.CenterHorizontally
            )
        ) {
            DateSlotButton(
                day = "Sat",
                date = "29",
                selected = true,
                disabled = false,
                onClick = {})
            DateSlotButton(
                day = "Sat",
                date = "29",
                selected = false,
                disabled = false,
                onClick = {})
            DateSlotButton(
                day = "Sat",
                date = "29",
                selected = true,
                disabled = true,
                onClick = {})
        }
    }
}
