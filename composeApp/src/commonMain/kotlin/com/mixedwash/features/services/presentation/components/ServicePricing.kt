package com.mixedwash.features.services.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.features.services.presentation.model.PricingMetadataPresentation
import com.mixedwash.ui.theme.Gray600
import com.mixedwash.ui.theme.Gray800
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_info
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServicePricing(
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

