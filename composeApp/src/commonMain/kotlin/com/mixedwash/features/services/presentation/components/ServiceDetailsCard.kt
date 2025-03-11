package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.features.local_cart.presentation.model.CartItemPresentation
import com.mixedwash.features.services.presentation.ServicesScreenEvent
import com.mixedwash.features.services.presentation.model.ItemPricingPresentation
import com.mixedwash.features.services.presentation.model.PricingMetadataPresentation
import com.mixedwash.features.services.presentation.model.ServiceDetailPresentation
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray600
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.dividerBlack
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_add
import mixedwash.composeapp.generated.resources.ic_arrow_right
import mixedwash.composeapp.generated.resources.ic_history
import mixedwash.composeapp.generated.resources.ic_info
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServiceDetail(
    service: ServicePresentation,
    onEvent: (ServicesScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
    serviceCartItems: List<CartItemPresentation>
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(18.dp)).background(Gray100)
    ) {

        AnimatedContent(
            targetState = service.imageUrl,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 90.dp, y = 40.dp)
        ) { imageUrl ->
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current).data(imageUrl)
                    .crossfade(true).build(), contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.width(200.dp).animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = service.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        letterSpacing = (-.48).sp,
                        color = BrandTheme.colors.gray.dark
                    )
                    val deliveryTime by remember(service) {
                        derivedStateOf {
                            "${service.deliveryTimeMinInHrs}${service.deliveryTimeMaxInHrs?.let { "-$it " } ?: " "}hrs"
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // duration
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_history),
                            contentDescription = null
                        )
                        AnimatedContent(
                            targetState = deliveryTime, transitionSpec = {
                                (slideInVertically { height -> height } + fadeIn()) togetherWith (slideOutVertically { height -> -height } + fadeOut())
                            }, label = "ServiceTransition"
                        ) { text ->

                            Text(
                                text = text,
                                fontSize = 14.sp,
                                letterSpacing = (-0.14).sp,
                                lineHeight = 16.8.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Text(
                    text = service.description.lowercase(),
                    color = Gray600,
                    fontSize = 14.sp,
                    lineHeight = 16.8.sp,
                )

                service.pricingMetadata?.let { pricingMetadata ->
                    PricingMetadataView(
                        pricingMetadata = pricingMetadata,
                    )
                }

                service.note?.let {
                    Text(
                        text = it.lowercase(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = Gray600
                    )
                }


            }

            HorizontalDivider(
                color = dividerBlack,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Check if Items Have Been Added
            if (serviceCartItems.isNotEmpty()) {
                serviceCartItems.forEach { item ->
                    CartItemEntry(
                        name = item.name,
                        quantity = item.quantity,
                        pricing = item.itemPricing,
                        onIncrement = { onEvent(ServicesScreenEvent.OnItemIncrement(item.itemId)) },
                        onDecrement = { onEvent(ServicesScreenEvent.OnItemDecrement(item.itemId)) }
                    )
                }
            } else {
                service.pricingMetadata?.let { pricingMetadata ->
                    Row(
                        modifier = Modifier
                            .clip(BrandTheme.shapes.button)
                            .clickable {
                                when (pricingMetadata) {
                                    is PricingMetadataPresentation.ServicePricingPresentation -> {
                                        onEvent(
                                            ServicesScreenEvent.OnItemAdd(
                                                itemId = service.items?.first()?.itemId ?: ""
                                            )
                                        )
                                    }

                                    is PricingMetadataPresentation.SubItemsPricingPresentation -> {
                                        onEvent(
                                            ServicesScreenEvent.OnOpenServiceItemsBottomSheet(
                                                serviceId = service.serviceId
                                            )
                                        )
                                    }

                                }
                            }
                            .background(BrandTheme.colors.gray.darker)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            4.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_add),
                            contentDescription = null,
                            tint = BrandTheme.colors.gray.lighter
                        )

                        Text(
                            "ADD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandTheme.colors.gray.lighter
                        )
                    }
                }
            }

            // add / remove
            /*
                        service.pricingMetadata?.let { pricingMetadata ->
                            Button(
                                onClick = {
                                    when (pricingMetadata) {
                                        is PricingMetadataPresentation.ServicePricingPresentation -> {
                                            onEvent(
                                                ServicesScreenEvent.OnItemAdd(
                                                    itemId = service.items?.first()?.itemId ?: ""
                                                )
                                            )
                                        }

                                        is PricingMetadataPresentation.SubItemsPricingPresentation -> {
                                            onEvent(
                                                ServicesScreenEvent.OnOpenServiceItemsBottomSheet(serviceId = service.serviceId)
                                            )
                                        }

                                    }
                                },
                                shape = BrandTheme.shapes.button,
                                colors = ButtonDefaults.buttonColors(containerColor = if (isItemAdded) Color.Transparent else BrandTheme.colors.gray.darker),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isItemAdded) Icons.Outlined.Delete else Icons.Default.Add,
                                        contentDescription = null,
                                        tint = if (isItemAdded) Gray800 else Gray100,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Text(
                                        text = if (isItemAdded) "Remove" else "Add",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        color = if (isItemAdded) Gray800 else Gray100
                                    )
                                }
                            }
                        }
            */

            HorizontalDivider(
                color = dividerBlack,
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            DetailsList(modifier = Modifier.padding(vertical = 18.dp), details = service.details)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(BrandTheme.shapes.card)
                    .clickable { onEvent(ServicesScreenEvent.OnProcessingDetailsClicked) }
                    .background(BrandTheme.colors.gray.c300)
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val contentColor = BrandTheme.colors.gray.c700
                    Text(
                        "Processing Details",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = contentColor
                    )
                    Text(
                        "Inclusions, Exclusions and recommendations",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = contentColor
                    )
                }
                Icon(
                    imageVector = vectorResource(resource = Res.drawable.ic_arrow_right),
                    modifier = Modifier.size(22.dp),
                    tint = BrandTheme.colors.gray.normal,
                    contentDescription = null
                )
            }
        }


        Box(
            modifier = Modifier.height(52.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            Pair(0f, Color.Transparent),
                            Pair(0.76f, BrandTheme.colors.gray.light),
                        )
                    )
                )
        )
    }
}

@Composable
private fun DetailsList(modifier: Modifier = Modifier, details: List<ServiceDetailPresentation>) {

    Column(modifier = modifier.fillMaxWidth().animateContentSize()) {
        Text(
            text = "Details",
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp,
            fontSize = 16.sp,
            color = BrandTheme.colors.gray.normalDark
        )
        Spacer(Modifier.height(8.dp))
        details.forEach { detail ->
            Column {
                DetailParameter(key = detail.key, value = detail.value)

                HorizontalDivider(color = dividerBlack, thickness = 0.5.dp)

            }
        }
    }
}

@Composable
private fun PricingMetadataView(
    pricingMetadata: PricingMetadataPresentation?,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "pricing",
                color = Gray600,
                fontSize = 12.sp,
                lineHeight = 16.8.sp
            )

            val priceText = buildAnnotatedString {
                when (val pricing = pricingMetadata) {
                    is PricingMetadataPresentation.ServicePricingPresentation -> {
                        "${pricing.pricePerUnit}/${pricing.unit}"
                        withStyle(
                            style = SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append("₹${pricing.pricePerUnit.div(100)}/")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append(pricing.unit)
                        }

                    }

                    is PricingMetadataPresentation.SubItemsPricingPresentation -> {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append("from ₹${pricing.startingPrice.floorDiv(100)}")
                        }

                    }

                    null -> {}
                }
            }

            Text(
                text = priceText,
                color = Gray800,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.8.sp
            )
        }
        pricingMetadata?.asServicePricing()?.let { pricing ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "minimum cart",
                        color = Gray600,
                        fontSize = 12.sp,
                        lineHeight = 16.8.sp
                    )
                    Icon(
                        modifier = Modifier.size(12.dp),
                        tint = BrandTheme.colors.gray.dark,
                        imageVector = vectorResource(Res.drawable.ic_info),
                        contentDescription = null
                    )
                }

                Text(
                    text = "₹${pricing.minimumPrice.div(100)} (${pricing.minimumUnits}${pricing.unit})",
                    color = Gray800,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
fun DetailParameter(key: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = key,
            color = Gray800,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.width(32.dp))

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
            Text(
                text = value,
                color = Gray800,
                style = BrandTheme.typography.subtitle1,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium, overflow = TextOverflow.Ellipsis,
                maxLines = 3,
            )
        }
    }
}

@Composable
private fun CartItemEntry(
    modifier: Modifier = Modifier,
    name: String,
    quantity: Int,
    pricing: ItemPricingPresentation,
    onIncrement: (() -> Unit)?,
    onDecrement: (() -> Unit)?
) {

    val description = when (pricing) {
        is ItemPricingPresentation.ServiceItemPricingPresentation -> {
            "1${pricing.unit} • ₹${pricing.pricePerUnit.div(100)}"
        }

        is ItemPricingPresentation.SubItemFixedPricingPresentation -> {
            "1pc • ₹${pricing.fixedPrice.div(100)}"
        }

        is ItemPricingPresentation.SubItemRangedPricingPresentation -> {
            "1pc • ₹${pricing.minPrice.div(100)} - ₹${pricing.maxPrice.div(100)}"
        }
    }

    val itemPrice = when (pricing) {
        is ItemPricingPresentation.ServiceItemPricingPresentation -> {
            "₹${pricing.minimumPrice.div(100)}"
        }

        is ItemPricingPresentation.SubItemFixedPricingPresentation -> {
            "₹${pricing.fixedPrice.div(100) * quantity}"
        }

        is ItemPricingPresentation.SubItemRangedPricingPresentation -> {
            "₹${pricing.minPrice.div(100) * quantity} - ₹${pricing.maxPrice.div(100) * quantity}"
        }
    }



    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp)
            Text(
                text = description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = BrandTheme.colors.gray.normalDark,
                lineHeight = 16.sp
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            ItemQuantityChip(
                isServiceItem = pricing is ItemPricingPresentation.ServiceItemPricingPresentation,
                quantity = quantity,
                showAddLabel = true,
                onIncrement = onIncrement,
                onDecrement = onDecrement
            )
            Text(
                text = itemPrice,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = BrandTheme.colors.gray.dark
            )
        }
    }
}

@Composable
private fun ItemQuantityChip(
    modifier: Modifier = Modifier,
    isServiceItem: Boolean,
    showAddLabel: Boolean = true,
    quantity: Int,
    onIncrement: (() -> Unit)?,
    onDecrement: (() -> Unit)?
) {
    Row(
        modifier = modifier
            .widthIn(min = 64.dp)
            .height(24.dp)
            .clip(BrandTheme.shapes.chip)
            .background(if (quantity > 0) Green else BrandTheme.colors.gray.darker),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val contentColor = BrandTheme.colors.gray.lighter

        if (quantity == 0) {
            Text("+", fontSize = 14.sp, color = contentColor)
            if (showAddLabel) {
                Text("ADD", fontSize = 12.sp, color = contentColor)
            }
            return@Row
        }

        if (!isServiceItem) {
            Text(
                "-",
                modifier = Modifier.noRippleClickable(onClick = onDecrement),
                fontSize = 12.sp,
                color = contentColor
            )
            Text(
                quantity.toString(),
                fontSize = 12.sp,
                color = contentColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                "+",
                modifier = Modifier.noRippleClickable(onClick = onIncrement),
                fontSize = 12.sp,
                color = contentColor
            )
        } else {
            Text(
                "-",
                modifier = Modifier.noRippleClickable(onClick = onDecrement),
                fontSize = 12.sp,
                color = contentColor,
                fontWeight = FontWeight.Medium
            )
            Text("ADDED", fontSize = 10.sp, color = contentColor, fontWeight = FontWeight.Medium)
        }
    }
}