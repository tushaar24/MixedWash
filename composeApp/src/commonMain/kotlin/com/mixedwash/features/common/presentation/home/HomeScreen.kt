package com.mixedwash.features.common.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.features.common.data.service.local.DummyData
import com.mixedwash.features.common.presentation.home.components.GettingStartedSection
import com.mixedwash.features.common.presentation.home.components.HomeScreenHeader
import com.mixedwash.features.common.presentation.home.components.OfferCard
import com.mixedwash.features.common.presentation.home.components.ServicesSection
import com.mixedwash.core.presentation.components.dump.OrderStatusCard
import kotlinx.coroutines.flow.collectLatest

/**
 * Notes:
 * 1. statusBarsPadding and navigationBarsPadding are used to prevent content from overlapping the
 *      systemUI in edge-to-edge
 * 2. the padding values aren't applied to the screen itself as the header color gradient must
 *    fill the entire width and all the height above it.
 * 3. the padding values are separately applied to each of the home screen components
 *      (Are there better ways of doing this?)
 */

val edgePadding = Modifier.padding(horizontal = 16.dp)

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        HomeScreenHeader(
            headerData = DummyData.headerData,
            onButtonClick = {},
        )

        OrderStatusCard(
            orderId = "#1022153",
            title = "Wash & Fold",
            subtitle = "Heavy Wash",
            description = "Your order is currently being washed at our facility",
            onDetailsClick = { },
            imageUrl = "silver_washing_machine",
            modifier = edgePadding
        )

        OfferCard(
            details = "Flat 20% OFF with coupon code WELCOME20 on your first order",
            imageUrl = "basket_overflow_black",
            buttonLabel = "Add to Cart",
            onButtonClick = { },
            modifier = edgePadding
        )

        // TODO: Replace Dummy with actual
        ServicesSection(
            serviceItems = DummyData.services,
            onSeeAll = {},
            modifier = edgePadding
        )

        GettingStartedSection(
            onExplore = {},
            onCall = {},
            modifier = edgePadding
        )
    }
}
