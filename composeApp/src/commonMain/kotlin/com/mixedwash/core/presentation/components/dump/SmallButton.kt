package com.mixedwash.core.presentation.components.dump

import BrandTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmallButton(
    onClick: () -> Unit,
    text: String,
    shape: Shape = RoundedCornerShape(12.dp),
    borderColor: Color = BrandTheme.colors.gray.darker,
    contentColor: Color = BrandTheme.colors.gray.darker,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    lineHeight: TextUnit = TextUnit.Unspecified,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    containerColor: Color = Color.Transparent,
    contentPadding: PaddingValues = PaddingValues(14.dp, 6.dp)
) {
    Button(
        modifier = modifier.indication(
            interactionSource = interactionSource,
            indication = null
        ),
        onClick = onClick,
        shape = shape,
        border = BorderStroke(1.dp, borderColor),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = contentPadding,
        ) {
        val fontFamily = BrandTheme.typography.body1.fontFamily
        Text(
            text = text,
            color = contentColor,
            fontWeight = fontWeight,
            fontSize = fontSize,
            fontFamily = fontFamily,
            lineHeight = lineHeight,
        )
    }
}