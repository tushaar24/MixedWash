package com.mixedwash.features.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.dump.LocationSlab
import com.mixedwash.ui.theme.Gray700

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    addressTitle: String,
    addressLine: String,
    onExpand: () -> Unit,
    onNotificationClick: () -> Unit,
    onFAQsClick: () -> Unit,
    contentColor: Color,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LocationSlab(
            contentColor = contentColor,
            addressKey = addressTitle,
            address = addressLine,
            onExpand = onExpand
        )

        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onFAQsClick) {
                Text(
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 6.dp),
                    text = "FAQs",
                    color = contentColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            VerticalDivider(
                color = contentColor.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .width(1.dp)
            )

            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = contentColor,
                modifier = Modifier.size(18.dp).clickable { onNotificationClick() },
            )
        }
    }
}