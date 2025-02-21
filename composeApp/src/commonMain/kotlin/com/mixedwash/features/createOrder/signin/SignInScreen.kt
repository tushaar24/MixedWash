package com.mixedwash.features.createOrder.signin

import BrandTheme
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.presentation.components.ShimmerText
import com.mixedwash.presentation.models.SnackbarHandler
import com.mixedwash.presentation.models.SnackbarPayload
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier.animateContentSize(),
    carouselItems: List<CarouselItem>,
    onSignInSuccess: () -> Unit,
    onSignInFailure: () -> Unit,
    ) {

    val scope = rememberCoroutineScope()
    val onFirebaseResult: (Result<FirebaseUser?>) -> Unit = { result ->
        if (!result.isSuccess) {
            onSignInFailure.invoke()
        } else {
            onSignInSuccess.invoke()
        }
    }


    val pagerState = rememberPagerState(pageCount = { carouselItems.size })
    val item by remember { derivedStateOf { carouselItems[pagerState.currentPage] } }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            scope.launch {
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % pagerState.pageCount,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioNoBouncy
                    )
                )
            }
        }
    }


    Column(
        modifier = modifier.fillMaxHeight().background(Color.Black).fillMaxWidth()
            .padding(horizontal = 45.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShimmerText(
            text = "Mixed Wash",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraLight,
            ),
            color = Color.Black,
            shimmerColor = BrandTheme.colors.gray.normalDark,
            durationMillis = 2000,
            delayMillis = 12000,
            isShimmering = true
        )

        Spacer(Modifier.height(72.dp))

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            // Use a stable key based on your data to help preserve individual page state.
            key = { page -> carouselItems[page].uid },
            pageSpacing = 100.dp,
            userScrollEnabled = false
        ) { index ->
            // Should move in from the top, y-offset = -32.dp
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Crossfade(targetState = carouselItems[index].imageUrl) {
                    AsyncImage(
                        modifier = Modifier.size(240.dp),
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        // TODO: Placeholder
                    )
                }
            }

        }



        Spacer(Modifier.height(32.dp))
        // should move in from the bottom, y-offst = +32.dp
        item.title?.let {
            Crossfade(targetState = it) {
                Text(
                    modifier = Modifier.animateContentSize().fillMaxWidth(),
                    text = it,
                    style = TextStyle(
                        color = BrandTheme.colors.gray.normal,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,

                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 28.sp,
                    )
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        item.description?.let {
            Crossfade(targetState = it) {
                Text(
                    text = it,
                    color = BrandTheme.colors.gray.normalDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.animateContentSize().fillMaxWidth(),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Spacer(Modifier.height(138.dp))

        AuthUiHelperButtonsAndFirebaseAuth(
            modifier = Modifier.width(IntrinsicSize.Max),
            onFirebaseResult = onFirebaseResult
        )

    }

}


data class CarouselItem @OptIn(ExperimentalUuidApi::class) constructor(
    val uid: String = Uuid.random().toHexString(),
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
)

@Composable
fun AuthUiHelperButtonsAndFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult, linkAccount = false) {
            GoogleSignInButton(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(44.dp),
                fontSize = 19.sp
            ) { this.onClick() }
        }

        AppleButtonUiContainer(onResult = onFirebaseResult, linkAccount = false) {
            AppleSignInButton(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(44.dp)
            ) { this.onClick() }
        }

    }

}

