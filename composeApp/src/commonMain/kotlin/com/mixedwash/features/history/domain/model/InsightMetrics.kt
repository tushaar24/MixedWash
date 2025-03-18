package com.mixedwash.features.history.domain.model

import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_drop
import mixedwash.composeapp.generated.resources.ic_hourglass
import mixedwash.composeapp.generated.resources.ic_washing_machine
import org.jetbrains.compose.resources.DrawableResource

data class InsightMetric(
    val metric: String,
    val unit: String,
    val icon: DrawableResource,
    var value: Int = 0
)

val insightMetrics = listOf(
    InsightMetric(
        metric = "time saved",
        unit = "hrs",
        icon = Res.drawable.ic_hourglass
    ),

    InsightMetric(
        metric = "washed",
        unit = "kg",
        icon = Res.drawable.ic_washing_machine
    ),

    InsightMetric(
        metric = "water saved",
        unit = "lts",
        icon = Res.drawable.ic_drop
    ),
)