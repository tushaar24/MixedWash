package com.mixedwash.presentation.components.dump

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray300
import com.mixedwash.ui.theme.Gray500
import com.mixedwash.ui.theme.Gray700
import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.dividerBlack
import com.mixedwash.presentation.components.gradient
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_progress_completed
import mixedwash.composeapp.generated.resources.ic_progress_pending
import mixedwash.composeapp.generated.resources.ic_progress_processing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderStatusCard(
    orderId: String,
    title: String,
    subtitle: String,
    description: String,
    onDetailsClick: () -> Unit,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clip(RoundedCornerShape(12.dp))) {

        Box(
            modifier = Modifier.fillMaxWidth()
                .gradient(colorStops = arrayOf(Pair(0f, Gray100), Pair(1f, Gray300)))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
            ) {
                Row(
                    verticalAlignment = CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column {
                            Text(
                                text = orderId,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                color = Gray500
                            )

                            Spacer(Modifier.padding(2.dp))

                            Text(
                                text = "$title • $subtitle",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Gray700
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Column {
                            Text(
                                text = description,
                                fontSize = 12.sp,
                                color = Gray700
                            )

                            Spacer(Modifier.height(6.dp))

                            UnderlineBox(lineColor = Gray700) {
                                Text(
                                    text = "View Details",
                                    fontSize = 12.sp,
                                    color = Gray700,
                                    modifier = Modifier.clickable { onDetailsClick() }
                                )
                            }
                        }
                    }

                    AsyncImageLoader(
                        imageUrl = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(74.dp)

                    )
                }

                Spacer(Modifier.height(16.dp))

                Divider(color = dividerBlack, modifier = Modifier.padding(8.dp))

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    ProgressStage("Pickup", 2)
                    ProgressStage("Process", 2)
                    ProgressStage("Wash", 1)
                    ProgressStage("Delivery", 0)
                }
            }

        }
    }
}

/**
 * @param state 0 -> pending, 1 -> in progress, 2 -> completed
 */
@Composable
fun ProgressStage(stageName: String, state: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = CenterVertically
    ) {
        when (state) {
            0 ->
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_progress_pending),
                    contentDescription = null,
                    tint = Gray700,
                )

            1 ->
                Image(
                    painter = painterResource(Res.drawable.ic_progress_processing),
                    contentDescription = null,
                )

            2 ->
                Box(modifier = Modifier.clip(CircleShape).background(color = Green)) {
                    Image(
                        painter = painterResource(Res.drawable.ic_progress_completed),
                        contentDescription = null
                    )
                }
        }

        Text(
            text = stageName,
            fontSize = 12.sp,
            color = Gray700,
            lineHeight = 14.4.sp
        )
    }
}