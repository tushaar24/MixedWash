package com.mixedwash.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

// Example String : "**only ₹95**/kg"

@Composable
fun processBoldText(
    text: String,
    defaultStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Normal),
    boldStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.SemiBold)
): AnnotatedString {
    val annotatedText = remember(text) {
        buildAnnotatedString {
            var startIndex = 0
            while (startIndex < text.length) {
                val boldStart = text.indexOf("**", startIndex)
                if (boldStart == -1) {
                    withStyle(defaultStyle){ append(text.substring(startIndex)) }
                    break
                }
                withStyle(defaultStyle) {
                    append(text.substring(startIndex, boldStart))
                }
                val boldEnd = text.indexOf("**", boldStart + 2)
                if (boldEnd == -1) {
                    withStyle(style = boldStyle) {
                        append(text.substring(boldStart))
                    }
                    break
                }
                withStyle(style = boldStyle) {
                    append(text.substring(boldStart + 2, boldEnd))
                }
                startIndex = boldEnd + 2
            }
        }
    }
    return annotatedText
}