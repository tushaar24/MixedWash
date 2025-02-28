package com.mixedwash.features.common.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.dump.AppButton
import com.mixedwash.core.presentation.components.dump.AsyncImageLoader
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.core.presentation.components.dropShadow
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray400
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.Gray900

@Composable
fun OfferCard(
    details: String,
    imageUrl: String,
    buttonLabel: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.padding(bottom = 8.dp)
            .dropShadow(shape = RoundedCornerShape(12.dp), color = Gray400, offsetY = 16.dp)
    ) {
        Box(
            modifier = Modifier.gradient(colorStops = arrayOf(Pair(0f, Gray800), Pair(1f, Gray900)))
                .padding(horizontal = 32.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = details,
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        color = Gray100
                    )

                    AppButton(
                        contentPadding = PaddingValues(14.dp, 6.dp),
                        onClick = onButtonClick,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        shape = RoundedCornerShape(6.dp),
                        backgroundColor = Gray100,
                        buttonTitle = buttonLabel
                    )
                }

                AsyncImageLoader(
                    imageUrl = imageUrl,
                    modifier = Modifier.height(72.dp).width(77.65.dp),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }

        }
    }
}