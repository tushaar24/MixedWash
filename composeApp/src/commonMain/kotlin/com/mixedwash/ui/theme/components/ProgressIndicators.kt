package com.mixedwash.ui.theme.components

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mixedwash.ui.theme.MixedWashTheme

@Composable
fun DefaultCircularProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        trackColor = Color.Transparent,
        color = BrandTheme.colors.primary.normalDark,
    )
}

//@Preview(showSystemUi = true)
@Composable
private fun PreviewProgressIndicators() {
    MixedWashTheme {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Default Circular Progress Indicator")
            DefaultCircularProgressIndicator()
        }
    }

}