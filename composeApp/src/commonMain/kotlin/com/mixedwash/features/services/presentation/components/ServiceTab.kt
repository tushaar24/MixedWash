package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.Gray100
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
//    Column(
//        modifier = modifier
//            .noRippleClickable(onClick = onClick)
//            .width(100.dp)
//            .clip(RoundedCornerShape(12.dp)),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
        val innerColor by animateColorAsState(if (isSelected) BrandTheme.colors.gray.light else BrandTheme.colors.gray.light)
        val outerColor by animateColorAsState(if (isSelected) BrandTheme.colors.gray.c200 else BrandTheme.colors.gray.light)
        val gradientBrush = Brush.radialGradient(0.45f to innerColor, 1f to outerColor)

        Box(
            modifier = modifier.size(80.dp).clip(RoundedCornerShape(9.6.dp))
                .background(if (isSelected) Gray100 else Color.Unspecified)
                .noRippleClickable { onClick() }
        ) {
            Box(
                modifier = Modifier
                    .padding(11.2.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(service.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.padding(7.2.dp).size(43.dp)
                )

                if (addedToCart) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(if (isSelected) Gray100 else Gray50)
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(modifier = Modifier.padding(3.dp).clip(CircleShape).background(Green)) {
                            Icon(
                                imageVector = Icons.Outlined.Done,
                                contentDescription = null,
                                tint = Gray50,
                                modifier = Modifier.size(20.dp).padding(2.dp).clip(CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }

//}
