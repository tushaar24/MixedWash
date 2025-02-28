package com.mixedwash.core.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mixedwash.ui.theme.largePadding
import com.mixedwash.ui.theme.Gray400
import com.mixedwash.ui.theme.Gray900
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.components.OutlinedButton

@Composable
fun DialogPopup(
    data: DialogPopupData,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(dismissOnBackPress = true, usePlatformDefaultWidth = false)) {
        val dialogShape = BrandTheme.shapes.roundCard
        Column(
            modifier = Modifier
                .shadow(
                    4.dp,
                    shape = dialogShape,
                )
                .clip(dialogShape)
                .background(BrandTheme.colors.background)
                .padding(vertical = 32.dp, horizontal = 28.dp)
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically)
        ) {

            data.apply {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(largePadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    icon?.let {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = it,
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        color = BrandTheme.colors.gray.dark,
                        style = BrandTheme.typography.subtitle1,
                        textAlign = TextAlign.Center
                    )

                    subtitle?.let {
                        Text(
                            it,
                            modifier = Modifier.fillMaxWidth(),
                            style = BrandTheme.typography.body1,
                        )
                    }
                }
                Spacer(Modifier.padding(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (secondaryButton != null) {
                        OutlinedButton(
                            text = secondaryButton.text,
                            iconBefore = secondaryButton.iconBefore,
                            iconAfter = secondaryButton.iconAfter,
                            contentColor = secondaryButton.contentColor,
                            outlineColor = secondaryButton.contentColor,
                            onClick = secondaryButton.onClick
                        )
                    }
                    if (primaryButton != null) {
                        OutlinedButton(
                            text = primaryButton.text,
                            iconBefore = primaryButton.iconBefore,
                            iconAfter = primaryButton.iconAfter,
                            contentColor = primaryButton.contentColor,
                            outlineColor = primaryButton.contentColor,
                            onClick = primaryButton.onClick
                        )
                    }
                }
            }

        }
    }
}


data class DialogPopupData(
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector? = null,
    val iconColor: Color = Gray400,
    val primaryButton: ButtonData? = null,
    val secondaryButton: ButtonData? = null,
    val onDismissRequest: () -> Unit
)

data class ButtonData(
    val text: String,
    val onClick: () -> Unit,
    val enabled: () -> Boolean = { true },
    val iconBefore: ImageVector? = null,
    val iconAfter: ImageVector? = null,
    val contentColor: Color = Gray900,
    val containerColor: Color = Color.Transparent,
)


//@Preview(showSystemUi = true)
@Composable
fun PreviewDialogPopup() {
    MixedWashTheme {

/*
        DialogPopup(
            DialogPopupData(
                title = "Are you sure you wish to delete the Home address?",
                subtitle = "The item will be added to your cart automatically",
                icon = Icons.Rounded.Warning,
                iconColor = BrandTheme.colors.secondary.normal,
                primaryButton = ButtonData(
                    text = "Delete",
                    onClick = {},
                    iconBefore = Icons.Rounded.Delete,
                    contentColor = RedDark,
                    containerColor = RedDark
                ),
                secondaryButton = ButtonData(text = "Cancel", onClick = {})
            )
        )
*/
    }
}

