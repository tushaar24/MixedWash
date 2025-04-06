package com.mixedwash.features.home.presentation

import BrandTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.mixedwash.SetStatusBarColor
import com.mixedwash.core.presentation.components.ClickableLoadingOverlay
import com.mixedwash.core.presentation.components.DialogPopup
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.components.DropShadowConfig
import com.mixedwash.core.presentation.components.ElevatedBox
import com.mixedwash.core.presentation.components.ShadowDirection
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.navigation.AppCloser
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.address.presentation.components.AddressList
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.components.HomeBanner
import com.mixedwash.features.home.presentation.components.HomeTopBar
import com.mixedwash.features.home.presentation.components.IntroSection
import com.mixedwash.features.home.presentation.components.OfferCard
import com.mixedwash.features.home.presentation.components.OrderStatusWidget
import com.mixedwash.features.home.presentation.components.ServiceUnavailable
import com.mixedwash.features.home.presentation.components.ServicesSection
import com.mixedwash.ui.theme.Gray50
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.components.IconButton
import com.mixedwash.ui.theme.screenHorizontalPadding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * # Notes:
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

    dialogPopupData?.let {
        DialogPopup(
            data = it,
            onDismissRequest = it.onDismissRequest,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    }


    val addressSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val serviceBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            newState != SheetValue.Hidden
        }
    )

    val appCloser = koinInject<AppCloser>()
    val scope = rememberCoroutineScope()
    ObserveAsEvents(uiEvents) { event ->
        when (event) {
            is HomeScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.payload)
            }

            is HomeScreenUiEvent.Navigate -> {
                navController.navigate(event.route)
            }

            HomeScreenUiEvent.CloseApp -> {
                appCloser.closeApp()
            }

            HomeScreenUiEvent.DismissAddressBottomSheet -> {
                scope.launch {
                    if (addressSheetState.isVisible) {
                        addressSheetState.hide()
                    }
                }.invokeOnCompletion {
                    onEvent(HomeScreenEvent.OnDismissedAddressBottomSheet)
                }
            }

            HomeScreenUiEvent.DismissAvailabilityBottomSheet -> {
                scope.launch {
                    if (serviceBottomSheetState.isVisible) {
                        serviceBottomSheetState.hide()
                    }
                }.invokeOnCompletion {
                    onEvent(HomeScreenEvent.OnAvailabilityBottomSheetDismissed)
                }
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
            sheetState = serviceBottomSheetState,
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false),
            shape = BrandTheme.shapes.rectangle,
            dragHandle = {},
            containerColor = BrandTheme.colors.background,
            contentColor = LocalContentColor.current,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                onEvent(HomeScreenEvent.OnCloseAppRequest)
            }
        ) {
            ServiceUnavailable(
                modifier = Modifier.fillMaxHeight(0.7f),
                unavailable = unavailable,
                onDismiss = { onEvent(HomeScreenEvent.OnCloseAppRequest) },
                onChangeLocation = { onEvent(HomeScreenEvent.OnChangeLocation) }
            )
        }
    }

    state.addressBottomSheetState?.let { state ->
        ModalBottomSheet(
            sheetState = addressSheetState,
            shape = BrandTheme.shapes.rectangle,
            dragHandle = {},
            containerColor = BrandTheme.colors.background,
            contentColor = LocalContentColor.current,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                onEvent(HomeScreenEvent.OnDismissedAddressBottomSheet)
            }) {

            Column(
                modifier = modifier.fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(
                        top = 36.dp,
                        start = screenHorizontalPadding,
                        end = screenHorizontalPadding
                    ),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = state.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    IconButton(
                        iconSize = 20.dp,
                        imageVector = Icons.Rounded.Close,
                        onClick = state.onClose,
                        buttonColors = BrandTheme.colors.iconButtonColors()
                            .copy(containerColor = Color.Transparent)
                    )

                }

                AddressList(
                    modifier = Modifier.fillMaxWidth(),
                    listState = rememberLazyListState(),
                    addresses = state.addresses,
                    isLoading = state.isLoading,
                    selectedAddressId = state.selectedAddressId,
                    onAddressClicked = state.onAddressClicked,
                    onSearchBoxClick = state.onSearchBoxClick,
                    onAddressEdit = state.onAddressEdit,
                    addressSearchState = state.addressSearchState
                )
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Logger.d("TAG", "Event: ${event.name}")
            if (event == Lifecycle.Event.ON_START) {
                onEvent(HomeScreenEvent.OnScreenStart)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
        ) {
            state.banner?.let { banner ->
                HomeBanner(
                    banner = banner,
                    statusBarHeight = statusBarHeight,
                    onBannerButtonClicked = { onEvent(HomeScreenEvent.OnBannerClick) },
                )
            }

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 32.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                state.activeOrders?.let {
                    OrderStatusWidget()
                }

                state.services?.let {
                    ServicesSection(
                        modifier = Modifier,
                        serviceItems = it,
                        onServiceClicked = { serviceId ->
                            onEvent(HomeScreenEvent.OnServiceClicked(serviceId))
                        }
                    )
                }

                state.offerCards?.get(0)?.let { offerCard ->
                    OfferCard(
                        text = offerCard.text,
                        description = offerCard.description,
                        imageUrl = offerCard.imageUrl,
                        gradient = offerCard.gradient,
                        contentColor = Color.parse(offerCard.contentTextColor),
                        onClick = { onEvent(HomeScreenEvent.OnOfferClick(offerCard.offerId)) }
                    )
                }

                state.introSection?.let {
                    IntroSection(
                        title = it.heading,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        buttonLabel = it.buttonText,
                        gradient = it.gradient,
                        contentColor = Color.parse(it.contentTextColor),
                        buttonTextColor = Color.parse(it.buttonTextColor),
                        onClick = { onEvent(HomeScreenEvent.OnIntroClick) },
                        modifier = Modifier,
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

            // lets the status bar content color be the same as top bar content color
            SetStatusBarColor(isLight = topBarContentColor.luminance() < 0.5f)

            val alpha = animateFloatAsState(
                targetValue = if (scrollValue <= startThreshold) 0f else 1f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
            Column(
                modifier = Modifier.align(Alignment.TopCenter).background(
                    color = Gray50.copy(alpha = alpha.value)
                )
            ) {
                Box(
                    modifier = Modifier.height(statusBarHeight + 2.dp).fillMaxWidth()
                )

                val elevation by animateDpAsState(if (scrollValue >= endThreshold) 4.dp else 0.dp)
                ElevatedBox(
                    dropShadowConfig = DropShadowConfig(
                        shadowColor = Gray50,
                        alpha = if (scrollValue >= endThreshold) 0.05f else 0f,
                        verticalDirection = ShadowDirection.Vertical.Bottom
                    ),
                    elevation = elevation,
                    backgroundColor = Color.Transparent,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    HomeTopBar(
                        addressTitle = if (state.cartAddress is CartAddressState.LocationFetched) state.cartAddress.address.title else "",
                        addressLine = if (state.cartAddress is CartAddressState.LocationFetched) state.cartAddress.address.toString() else "",
                        onLocationSlabClicked = { onEvent(HomeScreenEvent.OnLocationSlabClicked) },
                        onProfileClick = { onEvent(HomeScreenEvent.OnNavigateToProfile) },
                        onFAQsClick = { onEvent(HomeScreenEvent.OnNavigateToFaqs) },
                        contentColor = topBarContentColor,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }

    val loading by remember(state.isLoading, state.cartAddress) {
        derivedStateOf {
            state.isLoading || state.cartAddress is CartAddressState.Unassigned
        }
    }

    ClickableLoadingOverlay(loading)
}