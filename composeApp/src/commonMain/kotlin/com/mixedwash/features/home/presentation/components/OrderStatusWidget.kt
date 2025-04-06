package com.mixedwash.features.home.presentation.components

import BrandTheme.colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.ui.theme.Green

@Composable
fun OrderStatusWidget(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val textColorPrimary = colors.gray.dark
    val textColorSecondary = colors.gray.c600

    Column {
        HorizontalPager(pagerState) {
            Box(
                modifier = modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                colors.gray.c100,
                                colors.gray.c300
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.padding(end = 16.dp).weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "#1022153",
                                fontSize = 10.sp,
                                lineHeight = 14.4.sp,
                                color = textColorSecondary
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Wash and Fold",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 18.sp,
                                    color = textColorPrimary
                                )

                                Box(
                                    modifier = Modifier.size(12.dp)
                                        .clip(CircleShape)
                                        .background(textColorPrimary).padding(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = colors.gray.c100
                                    )
                                }
                            }

                            Text(
                                text = "Our captain is on his way to pickup your order",
                                minLines = 2,
                                lineHeight = 18.sp,
                                fontSize = 12.sp,
                                color = textColorSecondary
                            )
                        }

                        AsyncImage(
                            model = "https://assets-aac.pages.dev/assets/delivery_scooter.png",
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..4) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    contentDescription = null,
                                    tint = Green,
                                    modifier = Modifier.size(18.dp)
                                )

                                Text(
                                    text = "Placed",
                                    lineHeight = 14.4.sp,
                                    fontSize = 12.sp,
                                    color = textColorPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { idx ->
                val color =
                    if (pagerState.currentPage == idx) colors.gray.dark
                    else colors.gray.c400
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(4.dp)
                )
            }
        }
    }
}

