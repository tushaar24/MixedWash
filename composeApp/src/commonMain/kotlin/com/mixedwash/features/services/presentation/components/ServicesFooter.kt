package com.mixedwash.features.services.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray300
import com.mixedwash.ui.theme.Gray900

@Composable
fun ServicesFooter(
    selectedItemsSize: Int,
    onProceed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Gray100)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
/*
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_verified),
                        contentDescription = null,
                        tint = Green
                    )

                    Column {
                        Text(
                            text = "20% OFF 'FLAT20' applied",
                            lineHeight = 12.sp,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Gray800,
                        )

                        UnderlineBox(lineColor = Gray500) {
                            Text(
                                text = "modify",
                                color = Gray500,
                                lineHeight = 14.sp,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
*/

                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$selectedItemsSize services added",
                            color = Gray900,
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        /*
                                        IconButton(onClick = {}) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowUp,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                    */
                    }
                }

                DefaultButtonLarge(
                    onClick = onProceed,
                    text = "PROCEED",
                )
            }
        }

        HorizontalDivider(
            thickness = 0.2.dp,
            color = Gray300,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}