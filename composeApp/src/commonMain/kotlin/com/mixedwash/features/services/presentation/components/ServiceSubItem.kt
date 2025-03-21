package com.mixedwash.features.services.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun ServiceSubItem(
    modifier: Modifier,
    imageUrl: String,
    title: String,
    pricing: String,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onAdd: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier.size(104.dp)) {
            Box(
                Modifier
                    .size(100.dp)
                    .align(Alignment.TopStart)
                    .clip(shape = BrandTheme.shapes.card)
                    .background(BrandTheme.colors.gray.light)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier.align(Alignment.Center).size(80.dp)
                )
            }
            ItemQuantityChip(
                modifier = Modifier.align(Alignment.BottomEnd),
                isServiceItem = false,
                showAddLabel = false,
                quantity = quantity,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                onAdd = onAdd
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = pricing, fontWeight = FontWeight.Medium, fontSize = 12.sp)
        }
    }
}

