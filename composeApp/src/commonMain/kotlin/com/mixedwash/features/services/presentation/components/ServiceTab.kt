package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Box(
        modifier = modifier.size(80.dp).clip(BrandTheme.shapes.card)
            .background(if (isSelected) BrandTheme.colors.gray.c200 else BrandTheme.colors.background)
            .noRippleClickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.size(58.dp), contentAlignment = Alignment.Center) {
                val imageSize by animateFloatAsState(targetValue = if (isSelected) 48f else 44f)
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(service.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.padding().size(imageSize.dp)
                )

                if (addedToCart) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(if (isSelected) Gray100 else Gray50)
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier.padding(3.dp).clip(CircleShape)
                                .background(Green)
                        ) {
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

}