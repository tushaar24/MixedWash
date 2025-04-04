package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.features.local_cart.domain.model.CartItem
import com.mixedwash.features.services.presentation.ServicesScreenEvent
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.Gray50
import com.mixedwash.ui.theme.Gray600
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_arrow_right
import mixedwash.composeapp.generated.resources.ic_history
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServiceDetail(
    service: ServicePresentation,
    onEvent: (ServicesScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
    serviceCartItems: List<CartItem>
) {
    Box(modifier = modifier.background(Gray50)) {

        val scrollState = rememberScrollState()
        val startThreshold = 150.dp.value
        val endThreshold = 250.dp.value
        val scrollValue = scrollState.value.toFloat()

        LaunchedEffect(service) {
            scrollState.scrollTo(0)
        }

        AnimatedContent(
            targetState = service.imageUrl,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 90.dp, y = 40.dp)
        ) { url ->
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalPlatformContext.current).data(url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(
                    when {
                        scrollValue <= startThreshold -> 1f
                        scrollValue >= endThreshold -> 0f
                        else -> 1f - ((scrollValue - startThreshold) / (endThreshold - startThreshold))
                    }
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(end = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier.width(200.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = service.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        letterSpacing = (-1).sp,
                        color = BrandTheme.colors.gray.darker
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
                                fontSize = 12.sp,
                                letterSpacing = (-0.14).sp,
                                lineHeight = 16.8.sp,
                                fontWeight = FontWeight.Medium,
                                color = BrandTheme.colors.gray.darker
                            )
                        }
                    }
                }
                Text(
                    text = service.description.lowercase(),
                    color = Gray600,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp,
                    minLines = 3
                )

                service.pricingMetadata?.let { pricingMetadata ->
                    ServicePricing(
                        pricingMetadata = pricingMetadata,
                    )
                }

//                service.note?.let {
//                    Text(
//                        text = it.lowercase(),
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 12.sp,
//                        lineHeight = 16.sp,
//                        color = Gray600
//                    )
//                }


            }

            ServiceCartItemsList(serviceCartItems, onEvent, service)

            DetailsList(modifier = Modifier.padding(vertical = 18.dp), details = service.details)

            ServiceInfoTab(
                title = "Processing Details",
                description = "Inclusions, Exclusions and recommendations",
                onClick = { onEvent(ServicesScreenEvent.OnProcessingDetailsClicked) }
            )

            ServiceInfoTab(
                title = "Unsure about your load?",
                description = "Use our load estimator to get a rough weight approximate",
                onClick = {  }  // todo
            )

            Spacer(Modifier.height(32.dp))
        }

//        Box(
//            modifier = Modifier
//                .height(32.dp)
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colorStops = arrayOf(
//                            Pair(0f, Color.Transparent),
//                            Pair(1f, BrandTheme.colors.gray.light),
//                        ),
//                        startY = 0f,
//                    )
//                )
//        )

//        Box(
//            modifier = Modifier
//                .height(32.dp)
//                .align(Alignment.TopCenter)
//                .fillMaxWidth()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colorStops = arrayOf(
//                            Pair(0f, BrandTheme.colors.gray.light),
//                            Pair(1f, Color.Transparent),
//                        ),
//                        startY = 0f,
//                    )
//                )
//        )

    }
}

@Composable
fun ServiceInfoTab(
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(BrandTheme.shapes.card)
            .clickable { onClick() }
            .background(BrandTheme.colors.gray.c100)
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val contentColor = BrandTheme.colors.gray.c700
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp,
                color = contentColor
            )
            Text(
                text = description,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
                color = contentColor
            )
        }
        Icon(
            imageVector = vectorResource(resource = Res.drawable.ic_arrow_right),
            modifier = Modifier.size(22.dp),
            tint = BrandTheme.colors.gray.c400,
            contentDescription = null
        )
    }
}