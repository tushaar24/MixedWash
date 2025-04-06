package com.mixedwash.core.presentation.components

import BrandTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.ui.theme.mediumPadding
import com.mixedwash.ui.theme.screenHorizontalPadding
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BrandSnackbar(
    modifier: Modifier = Modifier,
    message: String,
    snackbarType: SnackBarType?,
    action: (() -> Unit) = {},
    actionText: String? = null
) {
    val type = snackbarType ?: SnackBarType.DEFAULT

    /**
     * SnackbarHost cannot animate Popup needed to display snackbar on top of ModalBottomSheet.
     * Temporary work-around to show enter - animation for snackbar.
     * */

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .dropShadow(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.1f),
                    blur = 4.dp,
                    spread = 0.dp,
                    offsetY = 4.dp,
                    offsetX = 0.dp
                )
                .background(Color.Black)
                .border(0.5.dp, BrandTheme.colors.gray.c500, RoundedCornerShape(12.dp))
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = vectorResource(type.icon),
                    contentDescription = null,
                    tint = when (type) {
                        SnackBarType.SUCCESS -> Color(0xFFD56363)
                        SnackBarType.WARNING -> Color.Yellow
                        SnackBarType.ERROR -> Color.Red
                        else -> Color.White
                    }
                )

                Text(
                    text = message,
                    color = BrandTheme.colors.gray.lighter,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    style = BrandTheme.typography.subtitle3.copy(lineHeight = 1.5.em),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                )

                Box(modifier = Modifier.padding(2.dp).clickable(onClick = action)) {
                    actionText?.let {
                        Text(
                            text = it,
                            style = BrandTheme.typography.subtitle3,
                            color = BrandTheme.colors.gray.lighter
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultSnackbarPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(mediumPadding),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenHorizontalPadding)
    ) {
        BrandSnackbar(
            message = "Message Delivered Successfully ",
            snackbarType = SnackBarType.SUCCESS,
            action = {},
            actionText = "Open Settings"
        )
        BrandSnackbar(
            message = "Message Delivered Successfully ",
            snackbarType = SnackBarType.WARNING,
            action = {},
            actionText = "Open Settings"
        )
        BrandSnackbar(
            message = "Message Delivered Successfully ",
            snackbarType = SnackBarType.ERROR,
            action = {},
            actionText = "Open Settings"
        )
        BrandSnackbar(
            message = "Message Delivered Successfully ",
            snackbarType = SnackBarType.DEFAULT,
            action = {},
            actionText = "Open Settings"
        )
    }
}