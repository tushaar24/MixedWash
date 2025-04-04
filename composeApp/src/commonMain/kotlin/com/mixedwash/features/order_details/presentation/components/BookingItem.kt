package com.mixedwash.features.order_details.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookingItem(
    title: String,
    description: String,
    annotatedPriceText: AnnotatedString?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text (
                text = title,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .weight(1f, fill = true)
                    .padding(end = 16.dp)
            )

            if (annotatedPriceText != null) {
                Text(
                    text = annotatedPriceText
                )
            }
        }

        // Add space between rows
//        Spacer(modifier = Modifier.height(6.dp))

        // Bottom row: Description and Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Description with weight to take available space
            Text(
                text = description,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f, fill = true)
                    .padding(end = 16.dp)
            )

        }
    }
}