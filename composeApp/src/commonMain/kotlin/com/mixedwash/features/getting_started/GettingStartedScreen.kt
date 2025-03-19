package com.mixedwash.features.getting_started

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
                navController.navigate(event.route)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${state.currentIndex + 1}",
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1).sp,
            lineHeight = 48.sp,
            fontSize = 48.sp,
            color = BrandTheme.colors.gray.c400,
            modifier = Modifier.align(Alignment.Start)
        )

        Box(contentAlignment = Alignment.Center) {

            Box(
                modifier = Modifier.clip(CircleShape)
                    .size(250.dp)
                    .background(state.currentItem.backgroundColor)
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(state.currentItem.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                error = painterResource(Res.drawable.ic_drop),
                modifier = Modifier.size(250.dp).offset(y = 10.dp)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = state.currentItem.title,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                lineHeight = 36.sp
            )

            Text(
                textAlign = TextAlign.Center,
                text = state.currentItem.description,
                fontSize = 14.sp,
                lineHeight = 24.sp
            )
        }

        Spacer(Modifier.fillMaxHeight(0.5f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            (0..<state.items.size).forEach { idx ->
                if (idx <= state.currentIndex) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_dot_filled),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_dot_outlined),
                        contentDescription = null,
                        tint = BrandTheme.colors.gray.c400
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        if (state.currentIndex + 1 < state.items.size) {
            Box(
                modifier = Modifier.align(Alignment.End)
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
        } else {
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
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        .background(BrandTheme.colors.gray.c900)
                        .padding(horizontal = 14.dp, vertical = 17.dp)
                        .noRippleClickable { onEvent(GettingStartedScreenEvent.OnExplore) }
                )
            }
        }
    }
}