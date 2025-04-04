package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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

@Composable
fun SlotsContainer(modifier: Modifier = Modifier, onSlotContainerClicked: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        SlotSummary(
            modifier = Modifier,
            title = "Order Pickup",
            description = "description",
            onSlotClicked = { onSlotContainerClicked() }
        )
    }
}

@Composable
private fun SlotSummary(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onSlotClicked: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = onSlotClicked),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = description,
                color = Color(0xFF4CAF50), // Green color
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "Drop Down Icon",
            modifier = Modifier.size(16.dp)
        )
    }
}