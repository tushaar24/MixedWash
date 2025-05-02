package com.mixedwash.features.order_details.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DetailsScreenHeaderContent(
    icon: DrawableResource,
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(44.dp)
                .padding(12.83.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(icon),
                contentDescription = null,
                tint = BrandTheme.colors.gray.dark,
                modifier = Modifier.size(18.33.dp)
            )
        }

        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
            )

            Text(
                text = text,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}