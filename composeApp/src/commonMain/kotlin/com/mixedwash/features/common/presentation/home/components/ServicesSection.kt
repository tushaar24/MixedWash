package com.mixedwash.features.common.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.mixedwash.features.common.presentation.services.model.ServiceItem
import com.mixedwash.core.presentation.components.dump.AsyncImageLoader
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray300
import com.mixedwash.ui.theme.Gray700
import com.mixedwash.core.presentation.components.gradient

@Composable
fun ServicesSection(
    serviceItems: List<ServiceItem>,
    modifier: Modifier = Modifier,
    onSeeAll: () -> Unit,
    textColor: Color = Gray700
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Services",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Text(
                text = "See All",
                color = textColor,
                modifier = Modifier.clickable { onSeeAll() },
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(18.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(serviceItems) {
                ServiceCard(it, onClick = {})
            }
        }
    }
}

@Composable
fun ServiceCard(serviceItem: ServiceItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.width(130.dp).clip(RoundedCornerShape(12.dp))
            .gradient(colorStops = arrayOf(Pair(1f, Gray100), Pair(1f, Gray300)))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 28.65.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImageLoader(
                imageUrl = serviceItem.imageUrl,
                contentDescription = null,
                modifier = Modifier.height(80.17.dp)
                    .width(100.dp),
                contentScale = ContentScale.Crop
            )

            // TODO: Confirm the font size here
            Text(
                text = serviceItem.title,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 10.17.dp)
            )
        }
    }
}