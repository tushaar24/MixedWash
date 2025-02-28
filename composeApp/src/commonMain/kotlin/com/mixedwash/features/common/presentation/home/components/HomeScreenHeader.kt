package com.mixedwash.features.common.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.features.common.presentation.home.model.HomeHeaderData
import com.mixedwash.core.presentation.components.gradient

@Composable
fun HomeScreenHeader(
    headerData: List<HomeHeaderData>,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val item = headerData[3]    // TODO: find ways to show diff. items at diff. times
    Box(modifier = Modifier.height(276.dp)) {
        Box(
            modifier = modifier.matchParentSize()
                .gradient(
                    colorStops = arrayOf(
                        Pair(0f, item.gradientLight),
                        Pair(1f, item.gradientDark)
                    )
                )
        )

        Column(
            modifier = Modifier.statusBarsPadding()
        ) {
            HomeTopBar(
                addressKey = "Home",
                address = "560095, Bengaluru",
                onExpand = {},
                onNotificationClick = {},
                onFAQsClick = {},
                textColor = item.textColor
            )

            HeaderContent(
                modifier = Modifier.padding(top = 30.5.dp),
                dataItem = item,
                onButtonClick = onButtonClick
            )
        }
    }
}