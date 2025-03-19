package com.mixedwash.features.getting_started

import BrandTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.ObserveAsEvents
import kotlinx.coroutines.flow.Flow
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_dot_filled
import mixedwash.composeapp.generated.resources.ic_dot_outlined
import mixedwash.composeapp.generated.resources.ic_drop
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun GettingStartedScreen(
    state: GettingStartedScreenState,
    onEvent: (GettingStartedScreenEvent) -> Unit,
    uiEvents: Flow<GettingStartedScreenUiEvent>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    ObserveAsEvents(uiEvents) { event ->
        when (event) {
            is GettingStartedScreenUiEvent.Navigate -> {
                navController.popBackStack()    // going back shouldn't return to this screen after navigating away
                navController.navigate(event.route)
            }
        }
    }

    val animatedBackgroundColor = animateColorAsState(
        targetValue = state.currentItem.backgroundColor,
        animationSpec = tween(durationMillis = 300)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(32.dp)
            .pointerInput(state.currentIndex) {
                var totalDrag = 0f
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        totalDrag += dragAmount.x
                    },
                    onDragEnd = {
                        // Check if the total drag exceeds a defined threshold (e.g., 100 pixels)
                        if (totalDrag < -100 && !state.lastPage) {
                            onEvent(GettingStartedScreenEvent.OnNext)
                        }
                        totalDrag = 0f
                    }
                )
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // current page number (hidden on the last page)
        AnimatedContent(
            targetState = "${state.currentIndex + 1}",
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn()) togetherWith (slideOutVertically { height -> -height } + fadeOut())
            },
            modifier = Modifier.align(Alignment.Start)
        ) { text ->

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp,
                lineHeight = 48.sp,
                fontSize = 48.sp,
                color = if (state.lastPage) Color.Transparent else BrandTheme.colors.gray.c400,
            )
        }

        // Title and description texts
        AnimatedContent(
            targetState = state.currentIndex,
            transitionSpec = {
                // When moving forward, slide in from the right and slide out to the left.
                (slideInHorizontally { width -> width } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                    .using(SizeTransform(clip = false))
            }
        ) {
            // Retrieve the current item based on the index.
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 19.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Image container
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(240.dp)
                            .background(animatedBackgroundColor.value),
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(state.currentItem.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        error = painterResource(Res.drawable.ic_drop),
                        modifier = Modifier.offset(y = 10.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))

                // Title
                Text(
                    text = state.currentItem.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    lineHeight = 36.sp
                )
                // Description
                Text(
                    textAlign = TextAlign.Center,
                    text = state.currentItem.description,
                    minLines = 4,
                    maxLines = 4,
                    color = BrandTheme.colors.gray.c700,
                    fontSize = 14.sp,
                    lineHeight = 24.sp
                )
            }
        }

        // Dots indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            (0 until state.items.size).forEach { idx ->
                if (idx <= state.currentIndex) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_dot_filled),
                        contentDescription = null,
                        tint = if (state.lastPage) Color.Transparent else Color.Unspecified
                    )
                } else {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_dot_outlined),
                        contentDescription = null,
                        tint = if (state.lastPage) Color.Transparent else BrandTheme.colors.gray.c400
                    )
                }
            }
        }

        // Action buttons (help center/explore or next arrow) based on the page state
        if (state.lastPage) {
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "HELP CENTER",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = BrandTheme.colors.gray.c900,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BrandTheme.colors.gray.c900, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 17.dp)
                        .noRippleClickable { onEvent(GettingStartedScreenEvent.OnNavigateToHelpCenter) }
                )
                Text(
                    text = "EXPLORE",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = BrandTheme.colors.gray.c50,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandTheme.colors.gray.c900)
                        .padding(horizontal = 14.dp, vertical = 17.dp)
                        .noRippleClickable { onEvent(GettingStartedScreenEvent.OnExplore) }
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "skip",
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    letterSpacing = (-1).sp,
                    modifier = Modifier.noRippleClickable { onEvent(GettingStartedScreenEvent.OnSkip) }
                )

                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(BrandTheme.colors.gray.darker)
                        .clickable { onEvent(GettingStartedScreenEvent.OnNext) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "next",
                        modifier = Modifier.size(32.dp).padding(2.dp),
                        tint = BrandTheme.colors.gray.c50
                    )
                }
            }
        }
    }
}