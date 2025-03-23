package com.mixedwash.features.home.presentation

import BrandTheme
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.mixedwash.SetStatusBarColor
import com.mixedwash.core.presentation.components.DialogPopup
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.navigation.AppCloser
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.components.HomeBanner
import com.mixedwash.features.home.presentation.components.HomeTopBar
import com.mixedwash.features.home.presentation.components.IntroSection
import com.mixedwash.features.home.presentation.components.OfferCard
import com.mixedwash.features.home.presentation.components.ServiceUnavailable
import com.mixedwash.features.home.presentation.components.ServicesSection
import com.mixedwash.ui.theme.Gray50
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.components.DefaultCircularProgressIndicator
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject

/**
 * Notes:
 * 1. statusBarsPadding and navigationBarsPadding are used to prevent content from overlapping the
 *      systemUI in edge-to-edge
 * 2. the padding values aren't applied to the screen itself as the header color gradient must
 *    fill the entire width and all the height above it.
 * 3. the padding values are separately applied to each of the home screen components
 *      (Are there better ways of doing this?)
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
    uiEvents: Flow<HomeScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    navController: NavController
) {


    var dialogPopupData by remember { mutableStateOf<DialogPopupData?>(null) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            newState != SheetValue.Hidden
        }
    )

    dialogPopupData?.let {
        DialogPopup(
            data = it,
            onDismissRequest = it.onDismissRequest,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    }


    val appCloser = koinInject<AppCloser>()
    ObserveAsEvents(uiEvents) { event ->
        when (event) {

            is HomeScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.payload)
            }

            is HomeScreenUiEvent.Navigate -> {
                navController.navigate(event.route)
            }

            HomeScreenUiEvent.NavigateUp -> {
                appCloser.closeApp()
            }

        }
    }


    if (state.cartAddress !is CartAddressState.LocationFetched) {
        dialogPopupData = when (state.cartAddress) {
            is CartAddressState.LocationError -> {
                state.cartAddress.dialogPopupData
            }

            else -> {
                null
            }
        }
    } else if (state.cartAddress.availability is ServiceAvailability.Unavailable) {
        val unavailable = state.cartAddress.availability
        ModalBottomSheet(
            sheetState = sheetState,
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false),
            shape = BrandTheme.shapes.rectangle,
            dragHandle = {},
            containerColor = BrandTheme.colors.background,
            contentColor = LocalContentColor.current,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                onEvent(HomeScreenEvent.OnDismissPermanentBottomSheet)
            }
        ) {
            ServiceUnavailable(
                unavailable = unavailable,
                onClick = { onEvent(HomeScreenEvent.OnDismissPermanentBottomSheet) })
        }
    }

    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }
    val scrollState = rememberScrollState()
    val startThreshold = 50.dp.value
    val endThreshold = 100.dp.value
    val scrollValue = scrollState.value.toFloat()

    Box {
        Column(
            modifier = modifier.navigationBarsPadding().verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            state.banner?.let { banner ->
                HomeBanner(
                    banner = banner,
                    statusBarHeight = statusBarHeight,
                    onBannerButtonClicked = { onEvent(HomeScreenEvent.OnBannerClick) },
                )
            }

            /*
            OrderStatusCard(
                orderId = "#1022153",
                title = "Wash & Fold",
                subtitle = "Heavy Wash",
                description = "Your order is currently being washed at our facility",
                onDetailsClick = { },
                imageUrl = "silver_washing_machine",
                modifier = edgePadding
            )
    */

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                state.introSection?.let {

                    IntroSection(
                        title = it.heading,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        buttonLabel = it.buttonText,
                        gradient = it.gradient,
                        contentColor = it.contentTextColor,
                        buttonTextColor = it.buttonTextColor,
                        onClick = { onEvent(HomeScreenEvent.OnIntroClick) },
                        modifier = Modifier,
                    )
                }

                val offerCard = state.offerCards?.get(0)
                offerCard?.let {
                    OfferCard(
                        text = offerCard.text,
                        bigText = offerCard.bigText,
                        imageUrl = offerCard.imageUrl,
                        buttonLabel = offerCard.buttonLabel,
                        gradient = offerCard.gradient,
                        contentColor = offerCard.contentTextColor,
                        buttonTextColor = offerCard.buttonTextColor,
                        onClick = { onEvent(HomeScreenEvent.OnOfferClick(offerCard.offerId)) }
                    )
                }

                state.services?.let {
                    ServicesSection(
                        modifier = Modifier.navigationBarsPadding(),
                        serviceItems = it,
                        onSeeAll = { onEvent(HomeScreenEvent.OnSeeAllServicesClicked) },
                        onServiceClicked = { serviceId ->
                            onEvent(
                                HomeScreenEvent.OnServiceClicked(
                                    serviceId
                                )
                            )
                        }
                    )
                }
            }
        }

        state.banner?.let { banner ->
            val topBarContentColor by animateColorAsState(
                targetValue = if (scrollValue <= startThreshold)
                    Color.parse(banner.uiTextColor)
                else
                    Gray800,
                animationSpec = tween(durationMillis = 300)
            )

            val targetStatusBarColor = if (scrollValue <= startThreshold) {
                // For example, use a transparent status bar (or any color you choose) when not scrolled
                Color.parse(banner.gradient.gradientColors[1].colorHex)
            } else {
                // Change to a solid color when scrolled
                Gray50
            }

// Optionally, animate the status bar color change
            val statusBarColor by animateColorAsState(
                targetValue = targetStatusBarColor,
                animationSpec = tween(durationMillis = 300)
            )

            SetStatusBarColor(isLight = statusBarColor.luminance() > 0.5f)

            Column(
                modifier = Modifier.align(Alignment.TopCenter).background(
                    color = Gray50.copy(
                        alpha = when {
                            scrollValue <= startThreshold -> 0f
                            else -> 1f
                        }
                    )
                )
            ) {
                Box(
                    modifier = Modifier.height(statusBarHeight + 2.dp).fillMaxWidth()
                )

                HomeTopBar(
                    addressTitle = if (state.cartAddress is CartAddressState.LocationFetched) state.cartAddress.address.title else "",
                    addressLine = if (state.cartAddress is CartAddressState.LocationFetched) state.cartAddress.address.pinCode else "",
                    onExpand = {},
                    onProfileClick = { onEvent(HomeScreenEvent.OnNavigateToProfile) },
                    onFAQsClick = { onEvent(HomeScreenEvent.OnNavigateToFaqs) },
                    contentColor = topBarContentColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }

    val loading by remember(state.isLoading, state.cartAddress) {
        derivedStateOf {
            state.isLoading || state.cartAddress is CartAddressState.Unassigned
        }
    }

    Crossfade(targetState = loading) {
        when (loading) {
            true -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .noRippleClickable(enabled = false, onClick = {}),
                contentAlignment = Alignment.Center
            ) {
                DefaultCircularProgressIndicator()
            }

            false -> {}
        }
    }
}