package com.mixedwash.features.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.mixedwash.ui.theme.Gray200
import com.mixedwash.ui.theme.Gray400
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun StatisticCard(
    value: Int,
    metric: String,
    unit: String,
    icon: DrawableResource,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 130.dp, minHeight = 110.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray200)
    ) {

        Column(
            modifier = modifier.matchParentSize()
                .padding(start = 16.dp, top = 22.dp, end = 16.dp, bottom = 18.dp)
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = value.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 26.sp
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = vectorResource(icon),
                        contentDescription = null,
                        modifier = Modifier.height(14.dp),
                        tint = Gray400
                    )

                    Text(
                        text = "$metric\n• $unit",
                        color = Gray400,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}