package com.mixedwash.features.services.presentation

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.services.presentation.components.ServiceDetail
import com.mixedwash.features.services.presentation.components.ServiceTab
import com.mixedwash.features.services.presentation.components.ServicesFooter
import com.mixedwash.features.services.presentation.components.SubItemsList
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.windowInsetsContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_faq
import mixedwash.composeapp.generated.resources.ic_question_mark
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    state: ServicesScreenState,
    onEvent: (ServicesScreenEvent) -> Unit,
    uiEventsFlow: Flow<ServicesScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val processingDetailsSheetState = rememberModalBottomSheetState()
    val loadEstimatorSheetState = rememberModalBottomSheetState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    state.subItemsListState?.let { subItemsListState ->
        ModalBottomSheet(
            onDismissRequest = { onEvent(ServicesScreenEvent.OnClosedSubItemsSheet) },
            sheetState = sheetState,
            dragHandle = {},
            containerColor = BrandTheme.colors.background,
            contentColor = LocalContentColor.current,
            shape = BrandTheme.shapes.bottomSheet
        ) {
            SubItemsList(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp)
                    .fillMaxHeight(0.85f),
                state = subItemsListState,
                onQuery = { onEvent(ServicesScreenEvent.OnSubItemsQuery(it)) },
                onItemIncrement = { onEvent(ServicesScreenEvent.OnItemIncrement(it)) },
                onItemDecrement = { onEvent(ServicesScreenEvent.OnItemDecrement(it)) },
                onItemAdd = { onEvent(ServicesScreenEvent.OnItemAdd(it)) },
                onFilterClick = { onEvent(ServicesScreenEvent.OnFilterClicked(it)) },
                onClose = { onEvent(ServicesScreenEvent.OnCloseSubItemsSheet) }
            )
        }
    }

    if (processingDetailsSheetState.isVisible && state.selectedServiceId != null) {
        val service = state.services.first { it.serviceId == state.selectedServiceId }
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = processingDetailsSheetState
        ) {
            Column(
                modifier = Modifier.background(BrandTheme.colors.background)
                    .padding(vertical = 48.dp, horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(BrandTheme.colors.gray.dark).padding(1.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            tint = BrandTheme.colors.gray.light,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(end = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Inclusions",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 18.sp,
                            color = BrandTheme.colors.gray.dark
                        )

                        Text(
                            text = service.inclusions ?: "none",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = BrandTheme.colors.gray.dark
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(BrandTheme.colors.gray.dark).padding(1.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = BrandTheme.colors.gray.light,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(end = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Exclusions",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 18.sp,
                            color = BrandTheme.colors.gray.dark
                        )

                        Text(
                            text = service.exclusions, fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = BrandTheme.colors.gray.dark
                        )
                    }
                }
            }

        }
    }

    if (loadEstimatorSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = loadEstimatorSheetState
        ) {
            Column(
                modifier = Modifier.background(BrandTheme.colors.background)
                    .padding(vertical = 48.dp, horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(36.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(BrandTheme.colors.gray.dark).padding(1.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_question_mark),
                            contentDescription = null,
                            tint = BrandTheme.colors.gray.light,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(end = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Not sure how many clothes you have?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 18.sp,
                            color = BrandTheme.colors.gray.dark
                        )

                        Text(
                            text = "One load of 6kg is roughly:\n12 Shirts\n3 Trousers\n7 Undergarments\n7 Pair of socks",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = BrandTheme.colors.gray.dark
                        )
                    }
                }

                AsyncImage(
                    model = "https://assets-aac.pages.dev/assets/white_washbaskt_overflow.png",
                    modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
        }
    }

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            ServicesScreenUiEvent.OpenProcessingDetailsBottomSheet -> {
                scope.launch {
                    if (!processingDetailsSheetState.isVisible) {
                        processingDetailsSheetState.show()
                    }
                }
            }

            is ServicesScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.payload)
            }

            ServicesScreenUiEvent.CloseSubItemsSheet -> {
                scope.launch {
                    if (sheetState.isVisible) {
                        sheetState.hide()
                    }
                }.invokeOnCompletion {
                    onEvent(ServicesScreenEvent.OnClosedSubItemsSheet)
                }
            }

            is ServicesScreenUiEvent.NavigateToRoute -> {
                navController.navigate(event.route)
            }

            ServicesScreenUiEvent.OpenLoadEstimatorBottomSheet -> {
                scope.launch {
                    if (!loadEstimatorSheetState.isVisible) {
                        loadEstimatorSheetState.show()
                    }
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().windowInsetsContainer()) {

        DefaultHeader(
            title = "",
            headingSize = HeadingSize.Subtitle1,
            headingAlign = HeadingAlign.Start,
            navigationButton = {
                HeaderIconButton(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    onClick = { navController.navigateUp() }
                )
            },
            actionButtons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                        .noRippleClickable { onEvent(ServicesScreenEvent.OnFaqClick) }
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_faq),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "faqs",
                        lineHeight = 18.sp,
                        fontSize = 14.sp,
                        letterSpacing = (-0.5).sp
                    )
                }
            }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = BrandTheme.colors.gray.c200,
        )
        Column(
            modifier = modifier
                .fillMaxSize(),
        ) {

            Row(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    itemsIndexed(state.services) { _, service ->
                        ServiceTab(
                            service = service,
                            addedToCart = state.cartItems.any { it.serviceId == service.serviceId },
                            isSelected = service.serviceId == state.selectedServiceId,
                            onClick = { onEvent(ServicesScreenEvent.OnServiceClick(service.serviceId)) },
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }

                VerticalDivider(
                    thickness = 1.dp,
                    color = BrandTheme.colors.gray.c200,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                state.selectedServiceId?.let { serviceId ->
                    val service =
                        state.services.first { service -> service.serviceId == serviceId }
                    ServiceDetail(
                        service = service,
                        onEvent = onEvent,
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(top = 16.dp),
                        serviceCartItems = state.cartItems.filter { it.serviceId == serviceId }
                    )
                }
            }

            if (state.cartItems.isNotEmpty()) {
                ServicesFooter(
                    selectedItemsSize = state.cartItems.fold(initial = 0) { acc, cartItem -> acc + cartItem.quantity },
                    onProceed = { onEvent(ServicesScreenEvent.OnSubmit) },
                )
            }
        }
    }
}
