package com.mixedwash.features.home.presentation.components

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mixedwash.core.presentation.components.dump.SmallButton
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.model.Gradient

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
            .padding(horizontal = 32.dp, vertical = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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


                    Text(
                        text = annotatedString,
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
                    modifier = Modifier.weight(0.6f),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }


}