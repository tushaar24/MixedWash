package com.mixedwash.features.home.presentation.components

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.dump.SmallButton
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.model.Gradient

@Composable
fun OfferCard(
    modifier: Modifier = Modifier,
    details: String,
    imageUrl: String,
    buttonLabel: String,
    gradient: Gradient,
    onClick: () -> Unit,
    contentColor: String,
    buttonTextColor: String
) {
        Box(
            modifier = modifier.clip(shape = BrandTheme.shapes.card).gradient(gradient = gradient)
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = details,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = Color.parse(contentColor)
                    )

                    SmallButton(
                        onClick = onClick,
                        contentColor = Color.parse(buttonTextColor),
                        containerColor = Color.parse(contentColor),
                        text = buttonLabel
                    )
                }

                AsyncImage(
                    model = imageUrl,
                    modifier = Modifier.height(72.dp).width(77.65.dp),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }

        }

}