package com.mixedwash.features.home.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.processBoldText
import com.mixedwash.features.home.presentation.model.HomeService
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray600

@Composable
fun ServicesSection(
    serviceItems: List<HomeService>,
    modifier: Modifier = Modifier,
    onServiceClicked: (String) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in serviceItems.indices step 2) {
            Row {
                ServiceCard(
                    serviceItem = serviceItems[i],
                    onClick = { onServiceClicked(serviceItems[i].serviceID) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                if (i + 1 < serviceItems.size - 1) {
                    ServiceCard(
                        serviceItem = serviceItems[i + 1],
                        onClick = { onServiceClicked(serviceItems[i + 1].serviceID) },
                        modifier = Modifier.weight(1f)
                    )
                } else { Spacer(Modifier.weight(1f)) }
            }
            if (i + 1 == 3) break
        }

        // View All Divider
        Box(
            modifier = Modifier.fillMaxWidth().noRippleClickable(onClick = { onServiceClicked("") }).padding(8.dp), contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.height(1.dp).fillMaxWidth().background(
                    brush = linearGradient(
                        colors = listOf(
                            BrandTheme.colors.gray.lighter,
                            BrandTheme.colors.gray.c300,
                            BrandTheme.colors.gray.lighter
                        )
                    )
                ),
            )
            Box(
                Modifier
                    .padding(horizontal = 8.dp)
                    .background(BrandTheme.colors.gray.lighter),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "view all",
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    color = BrandTheme.colors.gray.c500,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
fun ServiceCard(serviceItem: HomeService, onClick: () -> Unit, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier.height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .clickable(onClick = onClick)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.CenterVertically)
        ) {
            Text(
                text = serviceItem.title, fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold, lineHeight = 14.sp,
                color = BrandTheme.colors.gray.dark
            )

            // Example String : "**only ₹95**/kg"
            val annotatedText = processBoldText(serviceItem.description)

            Text(
                text = annotatedText, fontSize = 10.sp, lineHeight = 12.sp,
                color = Gray600
            )

        }

        val imageSize = (maxWidth.value * 0.30).dp
        AsyncImage(
            model = serviceItem.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(imageSize).offset((-4).dp, (-4).dp).align(Alignment.BottomEnd),
            contentScale = ContentScale.Fit
        )
    }
}

