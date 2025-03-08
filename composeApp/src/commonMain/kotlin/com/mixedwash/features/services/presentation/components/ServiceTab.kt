package com.mixedwash.features.services.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.dump.AsyncImageLoader
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray300
import com.mixedwash.ui.theme.Gray50
import com.mixedwash.ui.theme.Gray700
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.Gray900
import com.mixedwash.ui.theme.Green

@Composable
fun ServiceTab(
    service: ServicePresentation,
    addedToCart: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxHeight().width(100.dp).clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Gray900 else Gray50)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Box(
                    modifier = Modifier.clip(CircleShape)
                        .background(if (isSelected) Gray800 else Gray100),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImageLoader(
                        imageUrl = service.imageUrl,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                if (addedToCart) {

                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(if (isSelected) Gray900 else Gray50)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(modifier = Modifier.padding(4.dp).clip(CircleShape).background(Green)) {
                            Icon(
                                imageVector = Icons.Outlined.Done,
                                contentDescription = null,
                                tint = Gray50,
                                modifier = Modifier.padding(2.dp).size(16.dp).clip(CircleShape)
                            )
                        }
                    }
                }
            }

            Text(
                text = service.title,
                color = if (isSelected) Gray300 else Gray700,
                lineHeight = 16.sp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
