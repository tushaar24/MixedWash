package com.mixedwash.previews

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil3.toCoilUri
import com.mixedwash.features.createOrder.presentation.screens.DeliveryNotes
import com.mixedwash.screenHorizontalPadding
import com.mixedwash.screenVerticalPadding
import com.mixedwash.ui.theme.MixedWashTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeliveryNotesPreview() {

    var text by remember { mutableStateOf("") }
    val imageUri = remember<MutableState<Uri?>> { mutableStateOf(null) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri.value = uri
            }
        }


    MixedWashTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = screenHorizontalPadding, vertical = screenVerticalPadding)
        ) {
            DeliveryNotes(
                text = text,
                modifier = Modifier,
                onValueChange = { text = it },
                onCameraClick = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onImageClick = {
                    imageUri.value = null
                },
                imageUri = imageUri.value?.toCoilUri()
            )
        }
    }
}
