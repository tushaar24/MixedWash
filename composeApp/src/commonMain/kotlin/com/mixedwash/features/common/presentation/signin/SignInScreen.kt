package com.mixedwash.features.common.presentation.signin

import BrandTheme
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.WindowInsetsContainer
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_logo_apple
import mixedwash.composeapp.generated.resources.ic_logo_google
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
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
        var millis = 2000L
        while (true) {
            delay(millis)
            scope.launch {
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % pagerState.pageCount,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioNoBouncy
                    )
                )
            }
            millis = 5000
        }
    }

    WindowInsetsContainer(
        modifier = Modifier.background(Color.Black),
        statusBarIconsLight = false
    ) {
        Column(
            modifier = modifier.fillMaxSize()
                .padding(horizontal = 45.dp, vertical = 56.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Mixed Wash",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraLight,

                color = BrandTheme.colors.gray.normalDark,
            )

            Column {
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
                item.title?.let {
                    Crossfade(targetState = it) {
                        Text(
                            modifier = Modifier.animateContentSize().fillMaxWidth(),
                            text = it,
                            color = BrandTheme.colors.gray.normal,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 26.sp,
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                item.description?.let {
                    Crossfade(targetState = it) {
                        Text (
                            text = it,
                            color = BrandTheme.colors.gray.normalDark,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }


            Column {
                GoogleButtonUiContainerFirebase(
                    modifier = Modifier.fillMaxWidth(),
                    onResult = onFirebaseResult,
                    linkAccount = false
                ) {
                    SignInButton(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = Color.Transparent,
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        logo = Res.drawable.ic_logo_google,
                        text = "Sign in with Google",
                        onClick = ::onClick
                    )
                }

                Spacer(Modifier.height(16.dp))

                AppleButtonUiContainer(onResult = onFirebaseResult, linkAccount = false) {
                    SignInButton(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = BrandTheme.colors.gray.darker,
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        logo = Res.drawable.ic_logo_apple,
                        text = "Sign in with Apple",
                        onClick = ::onClick
                    )

                }
            }
        }
    }

}


data class CarouselItem @OptIn(ExperimentalUuidApi::class) constructor(
    val uid: String = Uuid.random().toHexString(),
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
)

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    borderColor: Color,
    containerColor: Color,
    contentColor: Color,
    logo: DrawableResource,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier
            .clip(BrandTheme.shapes.button)
            .border(width = 0.5.dp, color = borderColor, shape = BrandTheme.shapes.button)
            .background(containerColor)
            .clickable(onClick = onClick)
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.height(22.dp),
            imageVector = vectorResource(logo),
            contentDescription = null
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}

