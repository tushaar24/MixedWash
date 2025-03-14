package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.clearFocusOnKeyboardDismiss
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.camera
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DeliveryNotes(
    modifier: Modifier = Modifier,
    placeHolder: String = "Add notes for the delivery driver",
    text: String,
    onValueChange: (String) -> Unit,
    imageUri: Uri? = null,
    onCameraClick: (() -> Unit)? = null,
    onImageClick: (() -> Unit)? = null,
) {


    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = BrandTheme.shapes.textField)
            .border(
                border = BorderStroke(0.5.dp, BrandTheme.colors.gray.normal),
                shape = BrandTheme.shapes.textField
            )
            .padding(
                start = 16.dp, end = 12.dp, top = 18.dp, bottom = 18.dp
            )
    ) {


        var hasFocus by remember { mutableStateOf(false) }
        BasicTextField(value = text.ifBlank {
            if (hasFocus) "" else placeHolder
        },
            onValueChange = onValueChange,
            modifier = Modifier
                .onFocusChanged { hasFocus = it.hasFocus }
                .clearFocusOnKeyboardDismiss()
                .fillMaxWidth()
                .weight(1f),
            maxLines = 2, // Allow overflow content
            minLines = 2,
            textStyle = BrandTheme.typography.body2.copy(color = if (text.isBlank()) BrandTheme.colors.gray.normalDark else Color.Unspecified),
            singleLine = false)
        if (onCameraClick == null && onImageClick == null) return
        AnimatedContent(imageUri, label = "load image") {
            when (it) {
                null -> {
                    Box(
                        modifier = Modifier
                            .clip(BrandTheme.shapes.chip)
                            .clickable(
                                enabled = onCameraClick != null, onClick = onCameraClick!!
                            )
                            .height(48.dp)
                            .width(48.dp)
                    ) {

                        Icon(
                            imageVector = vectorResource(Res.drawable.camera),
                            contentDescription = "camera icon",
                            modifier = Modifier
                                .size(size = 32.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .clip(BrandTheme.shapes.chip)
                            .background(Color.Black)
                            .clickable(
                                enabled = onImageClick != null, onClick = onImageClick!!
                            )
                            .height(48.dp)
                            .width(48.dp)
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Uploaded Image",
                            clipToBounds = true,
                            alpha = 0.7f,
                            modifier = Modifier.clip(BrandTheme.shapes.chip),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.TopStart)
                                .clip(BrandTheme.shapes.chip),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Remove Image",
                            tint = BrandTheme.colors.gray.light,
                        )
                    }
                }

            }
        }
    }
}
