package com.mixedwash.core.presentation.components

import BrandTheme.colors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.features.home.presentation.components.OrderProgressStage
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_progress_completed_small
import mixedwash.composeapp.generated.resources.ic_progress_pending
import mixedwash.composeapp.generated.resources.ic_progress_processing
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderProgressRow(
    lastCompletedStage: OrderProgressStage,
    currentlyActiveStage: OrderProgressStage? = null,
    modifier: Modifier = Modifier
) {
    val textColorPrimary = if (currentlyActiveStage?.completed == true) colors.gray.c200 else colors.gray.dark
    val matteGreen = Color(0xFF8AAA6B)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrderProgressStage.entries.forEach { entry ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = vectorResource(
                        if (entry.ordinal <= lastCompletedStage.ordinal) Res.drawable.ic_progress_completed_small
                        else if (entry.ordinal == currentlyActiveStage?.ordinal) Res.drawable.ic_progress_processing
                        else Res.drawable.ic_progress_pending
                    ),
                    contentDescription = null,
                    tint = if (currentlyActiveStage?.completed == true) textColorPrimary else matteGreen,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = entry.displayName,
                    lineHeight = 14.4.sp,
                    fontSize = 12.sp,
                    color = textColorPrimary
                )
            }
        }
    }
}