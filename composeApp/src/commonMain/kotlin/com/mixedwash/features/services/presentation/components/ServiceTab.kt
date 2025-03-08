package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.Gray300
import com.mixedwash.ui.theme.Gray50
import com.mixedwash.ui.theme.Gray700
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
        Column(
            modifier = modifier.fillMaxHeight().padding(vertical = 10.dp).width(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.size(76.dp)) {
                Box(
                    modifier = Modifier.clip(CircleShape)
                        .background(if (isSelected) BrandTheme.colors.gray.light else Color.Transparent)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val imageSize by animateDpAsState(if (isSelected) 54.dp else 48.dp)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(service.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(imageSize)
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

            val height by animateDpAsState(targetValue = if (isSelected) 10.dp else 0.dp)
            Spacer(Modifier.height(height))

            Text(
                text = service.title,
                color = BrandTheme.colors.gray.dark,
                lineHeight = 16.sp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }

}
