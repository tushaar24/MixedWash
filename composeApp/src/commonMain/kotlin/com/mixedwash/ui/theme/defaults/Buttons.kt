package com.mixedwash.ui.theme.defaults

import BrandTheme
import BrandTheme.colors
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mixedwash.buttonCornerRadius
import com.mixedwash.ui.theme.MixedWashTheme

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String? = null,
    style: TextStyle = BrandTheme.typography.largeButton,
    colors: ButtonColors = BrandTheme.colors.buttonColors(),
    enabled: Boolean = true,
    borderStrokeColor: Color = Color.Transparent,
    iconBefore: ImageVector? = null,
    iconAfter: ImageVector? = null
) {
    Button(
        modifier = modifier.height(56.dp),
        onClick = onClick,
        colors = colors,
        enabled = enabled,
        border = BorderStroke(1.dp, borderStrokeColor),
        shape = BrandTheme.shapes.button
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconBefore != null) {
                Icon(
                    modifier = Modifier.height(style.fontSize.value.dp),
                    imageVector = iconBefore,
                    contentDescription = "icon",
                )
            }
            if (text != null) {
                Text(text, style = style)
            }
            if (iconAfter != null) {
                Icon(
                    modifier = Modifier.height(style.fontSize.value.dp),
                    imageVector = iconAfter,
                    contentDescription = "icon",
                )
            }
        }
    }
}


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String? = null,
    enabled: Boolean = true,
    iconBefore: ImageVector? = null,
    iconAfter: ImageVector? = null
) {
    val colors = BrandTheme.colors.buttonColors().copy(
        containerColor = BrandTheme.colors.gray.darker,
        contentColor = BrandTheme.colors.gray.lighter
    )

    val borderStrokeColor: Color = Color.Transparent

    return DefaultButton(
        modifier = modifier,
        onClick = onClick,
        text = text,
        colors = colors,
        enabled = enabled,
        borderStrokeColor = borderStrokeColor,
        iconBefore = iconBefore,
        iconAfter = iconAfter
    )
}


@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String? = null,
    enabled: Boolean = true,
    iconBefore: ImageVector? = null,
    iconAfter: ImageVector? = null
) {

    val colors = BrandTheme.colors.buttonColors().copy(
        containerColor = BrandTheme.colors.gray.darker,
        contentColor = BrandTheme.colors.gray.lighter,
    )


    return DefaultButton(
        modifier = modifier,
        onClick = onClick,
        text = text,
        colors = colors,
        enabled = enabled,
        borderStrokeColor = Color.Transparent,
        iconBefore = iconBefore,
        iconAfter = iconAfter
    )
}

@Composable
fun PrimaryTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String,
    enabled: Boolean = true,
    iconBefore: ImageVector? = null,
    iconAfter: ImageVector? = null
) {
    val colors = ButtonColors(
        containerColor = Color.Transparent,
        contentColor = BrandTheme.colors.primary.normalDark,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = BrandTheme.colors.gray.normalDark
    )
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = BrandTheme.shapes.button,
        colors = colors,
        enabled = enabled,
    ) {
        iconBefore?.let {
            Icon(
                modifier = Modifier.height(14.dp),
                imageVector = it,
                contentDescription = "icon",
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(text = text, style = BrandTheme.typography.mediumButton)
        iconAfter?.let {
            Spacer(Modifier.width(4.dp))
            Icon(
                modifier = Modifier.height(15.dp),
                imageVector = it,
                contentDescription = "icon",
            )
        }
    }
}

@Composable
fun SecondaryTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String,
    enabled: Boolean = true,
    iconBefore: ImageVector? = null,
    iconAfter: ImageVector? = null
) {
    val colors = ButtonColors(
        containerColor = Color.Transparent,
        contentColor = BrandTheme.colors.gray.lighter,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = BrandTheme.colors.gray.normalDark
    )
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = BrandTheme.shapes.button,
        colors = colors,
        enabled = enabled,
    ) {
        iconBefore?.let {
            Icon(
                modifier = Modifier.height(14.dp),
                imageVector = it,
                contentDescription = "icon",
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(text = text, style = BrandTheme.typography.mediumButton)
        iconAfter?.let {
            Spacer(Modifier.width(4.dp))
            Icon(
                modifier = Modifier.height(15.dp),
                imageVector = it,
                contentDescription = "icon",
            )
        }
    }
}


@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String? = null,
    enabled: Boolean = true,
    iconBefore: ImageVector? = null,
    iconAfter: ImageVector? = null,
    contentColor: Color = colors.gray.darker,
    outlineColor: Color = colors.gray.darker
) {
    val colors: ButtonColors =
        BrandTheme.colors.outlinedButtonColors().copy(contentColor = contentColor)
    val borderColor =
        if (enabled) outlineColor else BrandTheme.colors.gray.normal

    return DefaultButton(
        modifier = modifier,
        onClick = onClick,
        text = text,
        colors = colors,
        enabled = enabled,
        borderStrokeColor = borderColor,
        iconBefore = iconBefore,
        iconAfter = iconAfter
    )
}

@Composable
fun HeaderIconButton(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    imageVector: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val borderColor: Color = Color.Transparent
    val buttonColors = ButtonColors(
        containerColor = Color.Transparent,
        contentColor = colors.gray.darker,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = colors.gray.normalDark
    )

    IconButton(
        modifier = modifier,
        imageVector = imageVector,
        onClick = onClick,
        enabled = enabled,
        buttonColors = buttonColors,
        borderColor = borderColor,
        iconSize = size,
        shape = BrandTheme.shapes.circle
    )
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    iconSize: Dp = 24.dp,
    paddingSize: Dp = iconSize / 3,
    onClick: () -> Unit,
    buttonColors: ButtonColors = colors.iconButtonColors(),
    enabled: Boolean = true,
    borderColor: Color = Color.Transparent,
    shape: Shape = BrandTheme.shapes.button
) {

    val contentColor =
        if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor
    val containerColor =
        if (enabled) buttonColors.containerColor else buttonColors.disabledContainerColor

    Box(
        modifier = modifier
            .clip(shape)
            .border(
                width = 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(buttonCornerRadius),
            )
            .clickable(enabled = enabled, onClick = onClick)
            .background(containerColor)
            .padding(paddingSize),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = imageVector,
            contentDescription = null,
            tint = contentColor
        )
    }
}

//@Preview(showSystemUi = true)
@Composable
private fun PreviewButtons() {
    MixedWashTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DefaultButton(
                modifier = Modifier,
                onClick = {},
                text = "Button",
                enabled = true,
                iconBefore = Icons.Default.Add,
                iconAfter = Icons.Default.Search
            )
            DefaultButton(
                modifier = Modifier,
                onClick = {},
                text = "Button",
                enabled = false,
                colors = colors.buttonColors(),
                iconBefore = Icons.Default.Add,
                iconAfter = Icons.Default.Search,
            )
            PrimaryButton(
                modifier = Modifier,
                onClick = {},
                text = "Primary",
                enabled = true,
            )
            PrimaryButton(
                modifier = Modifier,
                onClick = {},
                text = "Primary",
                enabled = false,
            )

            SecondaryButton(
                modifier = Modifier,
                onClick = {},
                text = "Secondary",
            )

            SecondaryButton(
                modifier = Modifier,
                onClick = {},
                text = "Secondary",
                enabled = false
            )

            PrimaryTextButton(
                modifier = Modifier,
                onClick = {},
                text = "Primary Text",
            )
            PrimaryTextButton(
                modifier = Modifier,
                onClick = {},
                text = "Primary Text",
                enabled = false
            )

            SecondaryTextButton(
                modifier = Modifier,
                onClick = {},
                text = "Secondary Text",
            )
            SecondaryTextButton(
                modifier = Modifier,
                onClick = {},
                text = "Secondary Text",
                enabled = false
            )

            OutlinedButton(
                modifier = Modifier,
                onClick = {},
                text = "Outlined",
            )
            OutlinedButton(
                modifier = Modifier,
                onClick = {},
                text = "Outlined",
                enabled = false
            )

            Text("Header Icon Button")
            HeaderIconButton(
                modifier = Modifier,
                imageVector = Icons.Default.Add,
                onClick = {}
            )
            HeaderIconButton(
                modifier = Modifier,
                imageVector = Icons.Default.Add,
                onClick = {},
                enabled = false
            )
            Text("Default Icon Button")
            IconButton(
                modifier = Modifier,
                imageVector = Icons.Default.Add,
                onClick = {},
                enabled = true
            )
            IconButton(
                modifier = Modifier,
                imageVector = Icons.Default.Add,
                onClick = {},
                enabled = false
            )
        }
    }
}


