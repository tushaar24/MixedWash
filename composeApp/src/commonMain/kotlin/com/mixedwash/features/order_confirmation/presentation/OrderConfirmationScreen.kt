package com.mixedwash.features.order_confirmation.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.navigation.navigateWithArgs
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray500
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.flow.Flow
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OrderConfirmationScreen(
    modifier: Modifier = Modifier,
    state: OrderConfirmationScreenState,
    uiEvents: Flow<OrderConfirmationScreenUiEvent>,
    onEvent: (OrderConfirmationScreenEvent) -> Unit,
    navController: NavController,
) {

    ObserveAsEvents(uiEvents) { event ->
        when (event) {
            is OrderConfirmationScreenUiEvent.Navigate -> {
                navController.navigateWithArgs(event.navArgs)
            }
        }
    }

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/check_mark_animation.json").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(composition)
    val animationIsComplete by derivedStateOf { progress >= 1f }

    WindowInsetsContainer(modifier = Modifier.background(Color.Black), statusBarIconsLight = true) {

        AnimatedContent(
            targetState = animationIsComplete,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                fadeIn(tween(500)) togetherWith fadeOut(tween(500))
            }) { animationOnGoing ->
            when (animationOnGoing) {
                true -> {
                    Column(
                        modifier = modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(48.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.title,
                                fontSize = 24.sp,
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Gray100,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = state.description,
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                color = Gray100,
                                textAlign = TextAlign.Center
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                Text(
                                    text = "Order Status",
                                    fontSize = 14.sp,
                                    lineHeight = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Gray100,
                                    modifier = Modifier.clickable {
                                        onEvent(
                                            OrderConfirmationScreenEvent.OnOrderStatusClicked
                                        )
                                    }
                                )

                                Text(
                                    text = "Support",
                                    fontSize = 14.sp,
                                    lineHeight = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Gray100,
                                    modifier = Modifier.clickable {
                                        onEvent(
                                            OrderConfirmationScreenEvent.OnSupportClicked
                                        )
                                    }
                                )
                            }
                        }


                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(2.dp, Gray500),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { onEvent(OrderConfirmationScreenEvent.OnBackHome) }
                        ) {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = "BACK HOME",
                                lineHeight = 20.sp,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                color = Gray500
                            )
                        }
                    }
                }

                false -> {
                    Image(
                        modifier = Modifier.size(240.dp),
                        painter = rememberLottiePainter(
                            composition = composition,
                            progress = { progress }
                        ),
                        contentDescription = "Order Placed Animation"
                    )
                }
            }
            }
        }
    }


