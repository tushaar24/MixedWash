package com.mixedwash.features.createOrder.presentation.screens

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.mixedwash.screenHorizontalPadding
import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.defaults.PrimaryTextButton
import com.mixedwash.ui.theme.defaults.SecondaryButton
import com.mixedwash.ui.theme.defaults.SecondaryTextButton


data class OrderConfirmationScreenState(
    val onBackHome: () -> Unit,
    val title: String,
    val description: String,
    val onCheckOrderStatus: () -> Unit
)

@Composable
fun OrderConfirmationScreen(
    modifier: Modifier = Modifier,
    state: OrderConfirmationScreenState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().background(Color.Black)
            .padding(horizontal = screenHorizontalPadding)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "check-circle",
                modifier = Modifier
                    .requiredSize(size = 120.dp).background(Color.White),
                tint = Green
            )
            Text(
                text = state.title,
                textAlign = TextAlign.Center,
                style = BrandTheme.typography.h5,
                color = Color.White
            )
            Text(
                text = state.description,
                textAlign = TextAlign.Center,
                lineHeight = 1.5.em,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
            )

            SecondaryTextButton(text = "Check Order Status", onClick = state.onCheckOrderStatus)

        }
        SecondaryButton(text = "Back Home", onClick = state.onBackHome)
    }
}




