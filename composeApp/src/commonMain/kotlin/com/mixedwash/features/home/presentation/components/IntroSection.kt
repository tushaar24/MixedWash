package com.mixedwash.features.home.presentation.components

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.presentation.components.dump.SmallButton
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.features.home.presentation.model.Gradient
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_drop
import mixedwash.composeapp.generated.resources.ic_right_cheveron
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun IntroSection(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    imageUrl: String,
    buttonLabel: String,
    gradient: Gradient,
    contentColor: Color,
    buttonTextColor: Color
) {
    Box(
        modifier = modifier.fillMaxWidth().clip(BrandTheme.shapes.card).gradient(gradient),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = contentColor, lineHeight = 14.sp,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = description,
                    color = contentColor, lineHeight = 16.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(12.dp))

                SmallButton(
                    onClick = onClick,
                    containerColor = BrandTheme.colors.gray.darker,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = buttonLabel,
                            color = buttonTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            modifier = Modifier.size(16.dp),
                            tint = buttonTextColor,
                            imageVector = vectorResource(Res.drawable.ic_right_cheveron),
                            contentDescription = null
                        )
                    }
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                modifier = Modifier.size(140.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(Res.drawable.ic_drop),
                contentDescription = null
            )
        }
    }
}