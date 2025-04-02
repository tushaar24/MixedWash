package com.mixedwash.features.home.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.core.presentation.util.processBoldText
import com.mixedwash.features.home.presentation.model.Gradient

@Composable
fun OfferCard(
    modifier: Modifier = Modifier,
    text: String,
    description: String,
    imageUrl: String,
    gradient: Gradient,
    onClick: () -> Unit,
    contentColor: Color,
) {

    Row(
        modifier = modifier.fillMaxWidth().clip(shape = BrandTheme.shapes.card)
            .gradient(gradient = gradient)
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val normalStyle = SpanStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
            val bigStyle = SpanStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )

            val annotatedText = processBoldText(text, normalStyle, bigStyle)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = annotatedText,
                    lineHeight = 16.sp,
                    color = contentColor
                )

                Box(
                    modifier = Modifier.size(14.dp).clip(CircleShape).background(contentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = BrandTheme.colors.gray.lighter,
                    )
                }

            }

            Text(
                text = description,
                color = contentColor,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            )
        }

        AsyncImage(
            model = imageUrl,
            modifier = Modifier.size(58.dp),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
    }


}