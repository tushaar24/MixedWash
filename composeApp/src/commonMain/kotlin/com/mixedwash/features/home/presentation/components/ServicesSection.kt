package com.mixedwash.features.home.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import coil3.compose.AsyncImage
import com.mixedwash.features.home.presentation.model.HomeService
import com.mixedwash.ui.theme.Gray700

@Composable
fun ServicesSection(
    serviceItems: List<HomeService>,
    modifier: Modifier = Modifier,
    onSeeAll: () -> Unit,
    onServiceClicked: (String) -> Unit,
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
            items(serviceItems) { service ->
                ServiceCard(service, onClick = { onServiceClicked(service.serviceID) })
            }
        }
    }
}

@Composable
fun ServiceCard(serviceItem: HomeService, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(130.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BrandTheme.colors.gray.light)
            .clickable (onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)
        ) {
            AsyncImage(
                model = serviceItem.imageUrl,
                contentDescription = null,
                modifier = Modifier.height(80.dp)
                    .width(100.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = serviceItem.title,
                fontSize = 14.sp,
            )
        }
    }
}