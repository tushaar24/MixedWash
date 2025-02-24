package com.mixedwash.features.common.presentation.order_review

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.domain.util.capitalize
import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.common.presentation.slot_selection.Offer
import com.mixedwash.features.common.presentation.slot_selection.TimeSlot
import com.mixedwash.presentation.components.DefaultHeader
import com.mixedwash.presentation.components.HeadingAlign
import com.mixedwash.presentation.components.HeadingSize
import com.mixedwash.presentation.components.noRippleClickable
import com.mixedwash.presentation.util.formatTime
import com.mixedwash.presentation.util.getDayAndDate
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.components.PrimaryButton
import com.mixedwash.ui.theme.dividerBlack
import kotlin.random.Random

data class OrderReviewScreenState(
    val items: List<ServiceSummary>,
    val pickupSlot: TimeSlot,
    val dropSlot: TimeSlot,
    val deliveryAddress: Address,
    val paymentBreakup: List<Pair<String, String>>,
    val onEditSlot: () -> Unit,
    val onEditAddress: () -> Unit
)

data class ServiceSummary(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val title: String,
    val description: String,
    val price: String,
    val unit: String,
    val actionLabel: String,
    val action: () -> Unit
)

@Composable
fun OrderReviewScreen(
    modifier: Modifier = Modifier,
    state: OrderReviewScreenState,
) {
    WindowInsetsContainer {
        Column(modifier = modifier) {
            DefaultHeader(
                title = "Review your booking",
                headingSize = HeadingSize.Subtitle1,
                headingAlign = HeadingAlign.Start,
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        onClick = {}
                    )
                }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .padding(
                        start = screenHorizontalPadding,
                        end = screenHorizontalPadding,
                    )
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(Modifier.height(2.dp))
                Text(text = "Service Details", style = BrandTheme.typography.subtitle2)
                Column(
                    modifier = Modifier
                        .clip(BrandTheme.shapes.card)
                        .background(BrandTheme.colors.gray.light)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.items.forEachIndexed { index, item ->
                        ServiceItemContainer(
                            title = item.title,
                            description = item.description,
                            price = item.price,
                            unit = item.unit,
                            actionLabel = item.actionLabel,
                            onAction = item.action,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (index < state.items.size - 1) {
                            HorizontalDivider(
                                color = dividerBlack,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                Text(text = "Order Details", style = BrandTheme.typography.subtitle2)
                OrderDetailsSummary(
                    modifier = Modifier.fillMaxWidth(),
                    pickupSlot = state.pickupSlot,
                    dropSlot = state.dropSlot,
                    deliveryAddress = state.deliveryAddress,
                    onEditAddress = state.onEditAddress,
                    onEditSlot = state.onEditSlot
                )
                Text(text = "Payment Summary*", style = BrandTheme.typography.subtitle2)
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = ParagraphStyle(lineHeight = 1.2.em)) {
                            withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                append("Estimate based on the minimum allowed quantity per item. Items exceeding in weight are priced separately per kg.")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            ) {
                                withLink(link = LinkAnnotation.Url("#")) { append("\nCheck pricing details") }
                            }
                        }
                    })
                Column(
                    modifier = Modifier
                        .clip(BrandTheme.shapes.card)
                        .background(BrandTheme.colors.gray.light)
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.paymentBreakup.forEach { pair ->
                        val (item, cost) = pair
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item, style = BrandTheme.typography.body3)
                            Text(text = cost, style = BrandTheme.typography.body3)
                        }

                    }

                    HorizontalDivider(color = dividerBlack)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Estimated Total (minimum)",
                            style = BrandTheme.typography.body3,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$150.00",
                            style = BrandTheme.typography.body3,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }

                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    text = "CONFIRM ORDER",
                )


                Spacer(Modifier.height(2.dp))


            }

        }
    }
}


@Composable
fun ServiceItemContainer(
    title: String,
    description: String,
    price: String,
    unit: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {

    // TODO : FIX ME
/*
    ConstraintLayout(
        modifier = modifier.padding(vertical = 6.dp)
    ) {
        val (titleRef, descriptionRef, priceRef, actionRef) = createRefs()

        Text(
            text = title,
            style = BrandTheme.typography.subtitle3,
            modifier = Modifier.constrainAs(titleRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(priceRef.start, margin = 16.dp)
                width = Dimension.fillToConstraints
            })

        Text(text = description, style = TextStyle(
            fontSize = 12.sp
        ), modifier = Modifier.constrainAs(descriptionRef) {
            start.linkTo(parent.start)
            top.linkTo(titleRef.bottom, margin = 6.dp)
            end.linkTo(priceRef.start, margin = 16.dp)
            width = Dimension.fillToConstraints
        })

        if (price.isNotBlank()) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp,
                        fontFamily = Typography.Poppins,
                        fontWeight = FontWeight.W600
                    )
                ) { append(price) }
                withStyle(
                    style = SpanStyle(fontSize = 12.sp)
                ) { append("/$unit") }
            }, modifier = Modifier.constrainAs(priceRef) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            })
        }

        ActionText(
            modifier = Modifier.constrainAs(actionRef) {
                end.linkTo(parent.end)
                bottom.linkTo(descriptionRef.bottom)
            }, text = actionLabel, action = onAction
        )
    }
*/
}


//@Preview(showSystemUi = true)
@Composable
private fun PreviewServiceItem() {
    MixedWashTheme {
        Box(Modifier.fillMaxSize()) {
            ServiceItemContainer(title = "Wash and Fold",
                description = "Min 4kg · Segregated Wash",
                price = "₹95",
                unit = "kg",
                actionLabel = "Remove",
                modifier = Modifier.fillMaxWidth(),
                onAction = {})
        }
    }
}


@Composable
private fun ActionText(modifier: Modifier = Modifier, text: String, action: () -> Unit) {
    Box(
        modifier = modifier
            .noRippleClickable(onClick = action)
            .drawBehind {

                val strokeWidth = 0.5.dp.value * density
                val y = size.height - strokeWidth / 2

                drawLine(
                    color = Gray800, Offset(0f, y), Offset(size.width, y), strokeWidth
                )
            },
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp, lineHeight = 1.em
            ),
        )
    }

}


@Composable
private fun OrderDetailsSummary(
    modifier: Modifier = Modifier,
    pickupSlot: TimeSlot,
    dropSlot: TimeSlot,
    deliveryAddress: Address,
    onEditAddress: () -> Unit,
    onEditSlot: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(BrandTheme.shapes.card)
            .background(color = BrandTheme.colors.gray.light)
            .padding(
                horizontal = 16.dp, vertical = 18.dp
            )

    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp, bottom = 2.dp
                )
        ) {
            Text(
                text = "Slot Details",
                style = BrandTheme.typography.subtitle3.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(weight = 1f)
            )
            Box(
                contentAlignment = Alignment.Center
            ) {
                ActionText(text = "Edit", action = onEditSlot)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                modifier = Modifier.weight(weight = 0.5f)
            ) {
                Text(
                    text = "Pickup ",
                    style = BrandTheme.typography.body3.copy(fontWeight = FontWeight.Bold)
                )
                val (day, date) = pickupSlot.startTimeStamp.getDayAndDate()
                val startTime = pickupSlot.startTimeStamp.formatTime().run {
                    "$first $second"
                }
                val endTime = pickupSlot.endTimeStamp.formatTime().run {
                    "$first $second"
                }

                Text(
                    text = """
                        ${day.capitalize()}, $date 
                        $startTime - $endTime
                    """.trimIndent(), lineHeight = 1.5.em, style = TextStyle(
                        fontSize = 12.sp
                    )
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                modifier = Modifier.weight(weight = 0.5f)
            ) {
                Text(
                    text = "Drop",
                    style = BrandTheme.typography.body3.copy(fontWeight = FontWeight.Bold)
                )
                val (day, date) = dropSlot.startTimeStamp.getDayAndDate()
                val startTime = dropSlot.startTimeStamp.formatTime().run {
                    "$first - $second"
                }
                val endTime = dropSlot.endTimeStamp.formatTime().run {
                    "$first $second"
                }

                Text(
                    text = """
                        ${day.capitalize()}, $date 
                        $startTime - $endTime
                        """.trimIndent(), lineHeight = 1.5.em, style = TextStyle(
                        fontSize = 12.sp
                    )
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = dividerBlack)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp, bottom = 2.dp
                )
        ) {
            Text(
                text = "Delivery address",
                style = BrandTheme.typography.subtitle3.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(weight = 1f)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionText(text = "Edit", action = onEditAddress)
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp, Alignment.CenterHorizontally
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location Pin Icon",
                        modifier = Modifier.requiredSize(size = 14.dp)
                    )
                    Text(
                        text = deliveryAddress.title, style = BrandTheme.typography.caption2
                    )
                }
                val deliveryText = deliveryAddress.run {
                    addressLine1 + (if (addressLine1.isNotBlank()) "\n" else "") +
                            addressLine2 + (if (addressLine2.isNotBlank()) "\n" else "") +
                            addressLine3 + (if (addressLine3.isNotBlank()) "\n" else "") +
                            pinCode
                }
                Text(
                    text = deliveryText,
                    lineHeight = 1.5.em,
                    style = TextStyle(
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

//@Preview(showSystemUi = true)
@Composable
private fun PreviewOrderDetailsSummary() {
    MixedWashTheme {
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = screenHorizontalPadding)
        ) {
            OrderDetailsSummary(
                Modifier.align(Alignment.Center),
                onEditAddress = { },
                onEditSlot = { },
                deliveryAddress = Address(
                    title = "Office",
                    addressLine1 = "2342, Electronic City Phase 2",
                    addressLine2 = "Silicon Town, Bengaluru",
                    pinCode = "560100",
                    uid = "asnak"
                ),
                pickupSlot = TimeSlot(
                    startTimeStamp = 1736933400L, // 9:30 AM
                    endTimeStamp = 1736944200L,   // 12:00 PM
                    isAvailable = true, offersAvailable = listOf(
                        Offer(
                            title = "Flat 10% OFF",
                            subtitle = "10% off on SBI Credit Card",
                            code = "10%OFFSBI"
                        )
                    )
                ),
                dropSlot = TimeSlot(
                    startTimeStamp = 1736933400L, // 9:30 AM
                    endTimeStamp = 1736944200L,   // 12:00 PM
                    isAvailable = true, offersAvailable = listOf(
                        Offer(
                            title = "Flat 10% OFF",
                            subtitle = "10% off on SBI Credit Card",
                            code = "10%OFFSBI"
                        )
                    )
                )
            )
        }
    }
}
