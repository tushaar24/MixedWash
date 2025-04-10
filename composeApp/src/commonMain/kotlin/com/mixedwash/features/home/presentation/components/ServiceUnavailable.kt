package com.mixedwash.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.features.home.presentation.ServiceAvailability
import com.mixedwash.features.services.presentation.components.DefaultButtonLarge
import com.mixedwash.ui.theme.components.OutlinedButton

@Composable
fun ServiceUnavailable(
    modifier: Modifier = Modifier,
    unavailable: ServiceAvailability.Unavailable,
    onDismiss: () -> Unit,
    onChangeLocation: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 48.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(
            24.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(){
            Text(
                text = unavailable.title,
                fontSize = 22.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = unavailable.description,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = unavailable.currentLocationString,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(unavailable.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Error graphic",
            modifier = Modifier.size(240.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally){
            DefaultButtonLarge(text = "Change Location", onClick = onChangeLocation)
            OutlinedButton(text = unavailable.buttonText, onClick = onDismiss)
        }

    }
}