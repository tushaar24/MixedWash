package com.mixedwash.core.presentation.components.dump

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.noRippleClickable
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_location
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LocationSlab(
    modifier: Modifier = Modifier,
    addressTitle: String,
    addressText: String,
    onLocationClick: () -> Unit,
    contentColor: Color = Color.DarkGray
) {
    Row(
        modifier = modifier.noRippleClickable(onClick = onLocationClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_location),
            contentDescription = "Location",
            tint = contentColor,
            modifier = Modifier.size(18.dp),
        )

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = addressTitle,
                    color = contentColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(weight = 1f, fill=false)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = addressText,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                color = contentColor.copy(alpha = 0.8f),
                maxLines = 1,
                fontWeight = FontWeight.Normal,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}