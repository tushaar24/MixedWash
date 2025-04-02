package com.mixedwash.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.dump.LocationSlab
import com.mixedwash.core.presentation.components.noRippleClickable
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_person_filled
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    addressTitle: String,
    addressLine: String,
    onLocationSlabClicked: () -> Unit,
    onProfileClick: () -> Unit,
    onFAQsClick: () -> Unit,
    contentColor: Color,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LocationSlab(
            modifier.fillMaxWidth(0.4f),
            contentColor = contentColor,
            addressTitle = addressTitle,
            addressText = addressLine,
            onLocationClick = onLocationSlabClicked
        )

        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 6.dp)
                    .noRippleClickable { onFAQsClick() },
                text = "faqs",
                color = contentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            VerticalDivider(
                color = contentColor.copy(alpha = 0.1f),
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(0.8f)
                    .padding(vertical = 4.dp, horizontal = 0.dp)
            )

            Icon(
                imageVector = vectorResource(Res.drawable.ic_person_filled),
                contentDescription = "Profile",
                tint = contentColor,
                modifier = Modifier.size(18.dp).noRippleClickable(onClick = onProfileClick),
            )
        }
    }
}