package com.mixedwash.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.features.home.presentation.model.HomeService
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray600
import com.mixedwash.ui.theme.Gray700

@Composable
fun ServicesSection(
    serviceItems: List<HomeService>,
    modifier: Modifier = Modifier,
    onSeeAll: () -> Unit,
    onServiceClicked: (String) -> Unit,
    textColor: Color = Gray700
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in serviceItems.indices step 2) {
            Row {
                ServiceCard(
                    serviceItem = serviceItems[i],
                    onClick = { onServiceClicked(serviceItems[i].serviceID) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(10.dp))

                if (i + 1 < serviceItems.size - 1) {
                    ServiceCard(
                        serviceItem = serviceItems[i + 1],
                        onClick = { onServiceClicked(serviceItems[i + 1].serviceID) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ServiceCard(serviceItem: HomeService, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 32.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterVertically
            )
        ) {

            Text(
                text = serviceItem.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp
            )

            Text(
                text = serviceItem.description,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Gray600
            )

            Spacer(Modifier.height(32.dp))
        }

        AsyncImage(
            model = serviceItem.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(68.dp).padding(4.dp).align(Alignment.BottomEnd),
            contentScale = ContentScale.Fit
        )
    }
}