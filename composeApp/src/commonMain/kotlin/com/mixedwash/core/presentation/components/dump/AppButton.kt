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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray800

@Composable
fun AppButton(
    onClick: () -> Unit,
    buttonTitle: String,
    shape: Shape = RoundedCornerShape(8.dp),
    borderColor: Color = Gray800,
    titleColor: Color = Gray800,
    textStyle: TextStyle = BrandTheme.typography.smallButton,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: TextUnit = TextUnit.Unspecified,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    backgroundColor: Color = Gray100,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = contentPadding,
        ) {
        Text(
            text = buttonTitle,
            color = titleColor,
            style = textStyle,
            fontWeight = fontWeight,
            fontSize = fontSize,
            lineHeight = lineHeight,
        )
    }
}