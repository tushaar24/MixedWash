package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.Gray50
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
            modifier = modifier
                .noRippleClickable(onClick = onClick)
                .width(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val innerColor by animateColorAsState(if(isSelected) BrandTheme.colors.gray.light else BrandTheme.colors.gray.light)
            val outerColor by animateColorAsState(if (isSelected) BrandTheme.colors.gray.c200 else BrandTheme.colors.gray.light)
            val gradientBrush = Brush.radialGradient(0.45f to innerColor, 1f to outerColor)

            Box(modifier = Modifier.size(76.dp)) {
                Box(
                    modifier = Modifier.clip(CircleShape)
                        .background(gradientBrush)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val imageSize by animateDpAsState(if (isSelected) 54.dp else 44.dp)
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
                            .background(BrandTheme.colors.gray.lighter)
                            .size(28.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(modifier = Modifier.padding(4.dp).clip(CircleShape).background(Green)) {
                            Icon(
                                imageVector = Icons.Outlined.Done,
                                contentDescription = null,
                                tint = Gray50,
                                modifier = Modifier.padding(2.dp).clip(CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = service.title,
                color = if(isSelected) BrandTheme.colors.gray.dark else BrandTheme.colors.gray.c600,
                lineHeight = 16.sp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

}
