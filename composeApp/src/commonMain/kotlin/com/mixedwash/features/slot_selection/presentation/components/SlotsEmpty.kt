package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SlotsEmpty() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        text = "No slots found.",
        textAlign = TextAlign.Center,
        style = BrandTheme.typography.body2
    )
}
