package com.mixedwash.presentation.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.mixedwash.ui.theme.largePadding
import com.mixedwash.ui.theme.mediumPadding
import com.mixedwash.presentation.models.SnackBarType
import com.mixedwash.ui.theme.screenHorizontalPadding


@Composable
fun BrandSnackbar(
    modifier: Modifier = Modifier,
    message: String,
    snackbarType: SnackBarType?,
    action: (() -> Unit) = {},
    actionText: String? = null
) {
    val type = snackbarType ?: SnackBarType.INFO

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

        Column(
            modifier.padding(
                start = screenHorizontalPadding,
                end = screenHorizontalPadding,
                bottom = largePadding
            )
        ) {

            Box(modifier = Modifier.dropShadow(shape = BrandTheme.shapes.card, color = type.contentColor.copy(alpha = 0.1f), blur = 3.dp,  spread = 4.dp, offsetY = 0.dp, offsetX = 0.dp)){
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .border(color = BrandTheme.colors.gray.dark, width = 1.dp, shape = BrandTheme.shapes.card)
                        .clip(BrandTheme.shapes.card)
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = mediumPadding, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                    ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = type.icon,
                            tint = type.contentColor,
                            contentDescription = null,
                        )
                        Text(
                            text = message,
                            color = type.contentColor,
                            style = BrandTheme.typography.subtitle3.copy(lineHeight = 1.5.em),
                            maxLines = 2
                        )
                    }

                    Box(modifier = Modifier.padding(2.dp).clickable(onClick = action)) {
                        actionText?.let {
                            Text(
                                text = it,
                                style = BrandTheme.typography.subtitle3,
                                color = type.contentColor
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
            snackbarType = SnackBarType.INFO,
            action = {},
            actionText = "Open Settings"
        )

    }

}
}