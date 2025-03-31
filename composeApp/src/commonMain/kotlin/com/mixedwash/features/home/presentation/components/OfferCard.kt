package com.mixedwash.features.home.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.model.Gradient
import com.mixedwash.ui.theme.GreenDark

@Composable
fun OfferCard(
    modifier: Modifier = Modifier,
    text: String,
    bigText: List<String>,
    imageUrl: String,
    buttonLabel: String,
    gradient: Gradient,
    onClick: () -> Unit,
    contentColor: String,
    buttonTextColor: String
) {

    Row(
        modifier = modifier.fillMaxWidth().clip(shape = BrandTheme.shapes.card)
            .gradient(gradient = gradient)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val annotatedString = buildAnnotatedString {
                append(text)

                // Define your text styles
                val normalStyle = SpanStyle(
                    fontSize = 12.sp,
                    color = Color.parse(contentColor)
                )

                val bigStyle = SpanStyle(
                    fontSize = 16.sp,  // Larger font size for bigText items
                    fontWeight = FontWeight.SemiBold,
                    color = Color.parse(contentColor)
                )

                // Apply normal style to the entire text first
                addStyle(normalStyle, 0, text.length)

                // Then find and style each bigText substring
                for (bigTextItem in bigText) {
                    val startIndex = text.indexOf(bigTextItem)
                    if (startIndex >= 0) {
                        val endIndex = startIndex + bigTextItem.length
                        addStyle(bigStyle, startIndex, endIndex)
                    }
                }

                addStyle(
                    ParagraphStyle(
                        lineHeight = 16.sp
                    ),
                    0, text.length
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = annotatedString,
                    color = Color.parse(contentColor)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.parse(buttonTextColor),
                    modifier = Modifier.size(14.dp)
                        .padding(1.dp)
                        .clip(CircleShape)
                        .aspectRatio(1f)
                        .background(GreenDark)
                        .noRippleClickable { onClick() }
                )
            }

            // TODO: add to json
            Text(
                text = "flat discount applicable",
                color = GreenDark,
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