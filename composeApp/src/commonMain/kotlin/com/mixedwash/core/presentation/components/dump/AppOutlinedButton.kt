package com.mixedwash.core.presentation.components.dump

import BrandTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.mixedwash.ui.theme.Gray800

@Composable
fun AppOutlinedButton(
    onClick: () -> Unit,
    buttonTitle: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    borderColor: Color = Gray800,
    titleColor: Color = Gray800,
    textStyle: TextStyle = BrandTheme.typography.mediumButton,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    containerColor: Color = Color.Transparent,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
) {
    Button(
        modifier = modifier,
        contentPadding = contentPadding,
        onClick = onClick,
        shape = shape,
        interactionSource = interactionSource,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Text(
            text = buttonTitle,
            color = titleColor,
            style = textStyle,
            fontWeight = fontWeight,
            fontSize = fontSize,
            lineHeight = lineHeight,
            modifier = Modifier.indication(
                interactionSource = interactionSource,
                indication = null
            ),
            letterSpacing = letterSpacing
        )
    }
}