package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mixedwash.ui.theme.MixedWashTheme

@Composable
fun Month(modifier: Modifier = Modifier, month: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredWidth(width = 46.dp)
            .requiredHeight(height = 58.dp)
            .clip(shape = BrandTheme.shapes.chip)
            .background(color = BrandTheme.colors.gray.light)
    ) {
        Text(
            text = month.uppercase(),
            color = BrandTheme.colors.gray.dark,
            textAlign = TextAlign.Center,
            style = BrandTheme.typography.smallButton,
            modifier = Modifier
                .rotate(-90f)
                .fillMaxWidth()
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MonthPreview() {
    MixedWashTheme {
        Box(Modifier.fillMaxSize()) {
            Month(Modifier.align(Alignment.Center), "Jan")
        }
    }
}
