package com.mixedwash.core.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.mediumPadding
import com.mixedwash.ui.theme.screenHorizontalPadding

@Composable
fun DefaultHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationButton: (@Composable () -> Unit)? = null,
    headingSize: HeadingSize = HeadingSize.Subtitle1,
    headingAlign: HeadingAlign = HeadingAlign.Start,
    backgroundColor: Color = BrandTheme.colors.background,
    headerElevation: Dp = 0.dp,
    shadowAlpha: Float = 0.02f,
    actionButtons: (@Composable() (RowScope.() -> Unit))? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RectangleShape,
                offsetY = headerElevation,
                color = Color.Black.copy(shadowAlpha),
                spread = 0.dp,
                blur = headerElevation
            ).background(backgroundColor)
            .zIndex(1f)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationButton?.let {
            Row(horizontalArrangement = Arrangement.Start) {
                it.invoke()
                Spacer(Modifier.padding(start = 16.dp))
            }
        }
        Text(
            modifier = Modifier
                .basicMarquee(velocity = 60.dp)
                .weight(1f),
            textAlign = headingAlign.textAlign,
            text = title ?: "",
            maxLines = 1,
            overflow = TextOverflow.Clip,
            style = headingSize.textStyle,
            color = BrandTheme.colors.gray.darker
        )
        actionButtons?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = mediumPadding,
                    alignment = Alignment.End
                )
            ) {
                Spacer(Modifier.padding(start = 2.dp))
                actionButtons()
            }
        }
    }
}


enum class HeadingSize {
    H5, Subtitle1;

    val textStyle: TextStyle
        @Composable
        get() = when (this) {
            H5 -> BrandTheme.typography.h5
            Subtitle1 -> BrandTheme.typography.subtitle1
        }
}

enum class HeadingAlign {
    Center, Start;

    val textAlign: TextAlign
        @Composable
        get() = when (this) {
            Center -> TextAlign.Center
            Start -> TextAlign.Start
        }
}

//@Preview
@Composable
fun PreviewHeader() {
    Column(modifier = Modifier.padding(horizontal = screenHorizontalPadding)) {
        DefaultHeader(
            title = "Pickup and Delivery Address",
            navigationButton = {
                HeaderIconButton(imageVector = Icons.Rounded.KeyboardArrowLeft, onClick = {})
            },
            headingAlign = HeadingAlign.Center,
            headingSize = HeadingSize.Subtitle1,
            actionButtons = {
                HeaderIconButton(
                    imageVector = Icons.Rounded.AccountCircle,
                    onClick = {},
                    enabled = true
                )
            },
        )
    }
}