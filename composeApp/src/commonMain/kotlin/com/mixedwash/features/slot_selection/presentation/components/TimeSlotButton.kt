package com.mixedwash.features.slot_selection.presentation.components

import BrandTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.MixedWashTheme

@Composable
fun TimeSlotButton(
    modifier: Modifier = Modifier,
    caption: String? = null,
    startTime: String,
    startTimeSuffix: String,
    endTime: String,
    endTimeSuffix: String,
    onClick: () -> Unit = {},
    selected: Boolean = false,
    unavailable: Boolean = false,
) {
    val topPadding = if (caption.isNullOrBlank()) 0.dp else 4.dp
    val contentColor = if (unavailable) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        BrandTheme.colors.gray.dark
    } else {
        BrandTheme.colors.gray.light
    }

    val strokeColor = if (unavailable) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        BrandTheme.colors.gray.normalDark
    } else {
        Color.Transparent
    }

    val containerColor = if (selected) {
            BrandTheme.colors.gray.darker
        } else {
            Color.Transparent
        }


    val subtitleColor = if (unavailable) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        Green
    } else {
        BrandTheme.colors.gray.light
    }

    val clickable = if (unavailable) {
        false
    } else if (!selected) {
        true
    } else {
        false
    }



    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredHeight(height = 58.dp)
            .clip(shape = BrandTheme.shapes.card)
            .border(
                border = BorderStroke(0.5.dp, strokeColor), shape = BrandTheme.shapes.card
            )
            .clickable(enabled = clickable, onClick = onClick)
            .background(containerColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = topPadding)
        ) {
            val text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(startTime) }
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(" $startTimeSuffix  -  ") }
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(endTime) }
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(" $endTimeSuffix") }
            }
            Text(
                text = text, modifier = Modifier, overflow = TextOverflow.Clip, maxLines = 1
            )
            caption?.let {
                Text(
                    text = it,
                    color = subtitleColor,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = 1.em,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimeSlotButtonPreview() {
    MixedWashTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                8.dp, alignment = Alignment.CenterVertically
            )
        ) {
            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = true,
                unavailable = false,
                caption = "10% Off"
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = false,
                caption = "1 offer"
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = false,
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = true,
                caption = "slot is full"
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = true,
            )


        }
    }
}
