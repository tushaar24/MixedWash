package com.mixedwash.features.common.presentation.profile

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.RedDark
import com.mixedwash.ui.theme.Typography.Companion.Poppins
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.components.IconButton
import com.mixedwash.ui.theme.dividerBlack
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.ui.theme.screenBottomSpacing
import com.mixedwash.ui.theme.screenHorizontalPadding
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_clothes_hanger
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

data class ProfileSection(
    val title: String,
    val items: List<ProfileSectionItem>
)

data class ProfileSectionItem(
    val resource: DrawableResource,
    val text: String,
    val onClick: (() -> Unit)?,
    val comingSoon: Boolean = false
)

data class ProfileScreenState(
    val sections: List<ProfileSection>,
    val imageUrl: String? = null,
    val name: String?,
    val email: String? = null,
    val phone: String?,
    val appName: String,
    val appVersion: String,
    val onLogout: () -> Unit,
    val onEditProfile: () -> Unit,
)

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileScreenState
) {
    WindowInsetsContainer {
        Column(modifier = modifier) {
            DefaultHeader(
                title = "Profile",
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
                modifier = Modifier
                    .padding(horizontal = screenHorizontalPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Spacer(modifier = Modifier.height(headerContentSpacing))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (state.imageUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(state.imageUrl)
                                    .crossfade(true)
                                    .build(), contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .clip(shape = BrandTheme.shapes.circle)
                                    .size(72.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                modifier = Modifier
                                    .clip(shape = BrandTheme.shapes.circle)
                                    .background(BrandTheme.colors.gray.c300)
                                    .size(72.dp)
                                    .padding(12.dp),
                                contentDescription = "Profile Picture",
                                tint = BrandTheme.colors.gray.normalDark
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top)) {
                            Text(
                                text = state.name ?: "User",
                                style = BrandTheme.typography.subtitle2
                            )
                            Spacer(Modifier.height(2.dp))

                            val style = BrandTheme.typography.body3.copy(fontFamily = Poppins())
                            if (!state.phone.isNullOrEmpty()) {
                                Text(
                                    text = state.phone,
                                    color = BrandTheme.colors.gray.normalDark,
                                    style = style
                                )
                            }

                            state.email?.let {
                                Text(
                                    text = it,
                                    color = BrandTheme.colors.gray.normalDark,
                                    style = style
                                )
                            }
                        }

                    }

                    IconButton(
                        imageVector = Icons.Rounded.Edit,
                        buttonColors = BrandTheme.colors.iconButtonColors().copy(
                            containerColor = Color.Transparent,
                            contentColor = BrandTheme.colors.gray.normalDark
                        ),
                        iconSize = 16.dp,
                        paddingSize = 8.dp,
                        onClick = state.onEditProfile
                    )

                }

                state.sections.forEach { section ->
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = section.title,
                        style = BrandTheme.typography.subtitle2,
                        color = BrandTheme.colors.gray.darker
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .clip(BrandTheme.shapes.card)
                            .background(BrandTheme.colors.gray.light)
                            .padding(vertical = 18.dp, horizontal = 16.dp),
                    ) {
                        section.items.forEachIndexed { index, profileScreenItem ->
                            ProfileScreenItem(
                                resource = profileScreenItem.resource,
                                text = profileScreenItem.text,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .noRippleClickable(
                                        enabled = profileScreenItem.onClick != null,
                                        onClick = profileScreenItem.onClick
                                    ),
                                comingSoon = profileScreenItem.comingSoon
                            )
                            if (index < section.items.size - 1) {
                                HorizontalDivider(color = dividerBlack)
                            }
                        }
                    }
                }

                Column(Modifier.padding(vertical = 16.dp)) {
                    Text(
                        text = state.appName,
                        style = BrandTheme.typography.subtitle3.copy(fontWeight = FontWeight.SemiBold),
                        color = BrandTheme.colors.gray.c700
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.appVersion,
                        style = BrandTheme.typography.body3
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier.noRippleClickable(onClick = state.onLogout),
                        text = "Logout",
                        style = BrandTheme.typography.subtitle2.copy(fontWeight = FontWeight.SemiBold),
                        color = RedDark
                    )


                }


                Spacer(modifier = Modifier.height(screenBottomSpacing))
            }

        }
    }
}

@Composable
fun ProfileScreenItem(
    modifier: Modifier = Modifier,
    resource: DrawableResource,
    text: String,
    comingSoon: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 6.dp)
            .alpha(if (comingSoon) 0.6f else 1f)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = vectorResource(resource),
                contentDescription = "icon",
                tint = BrandTheme.colors.gray.c700,
                modifier = Modifier.size(24.dp)
            )
            val annotatedString = buildAnnotatedString {
                withStyle(style = BrandTheme.typography.subtitle3.toSpanStyle()) {
                    append(text)
                }
                if (comingSoon) {
                    withStyle(
                        style = BrandTheme.typography.subtitle3.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                            .toSpanStyle()
                    ) {
                        append(" (coming soon)")
                    }
                }

            }
            Text(
                text = annotatedString,
                color = BrandTheme.colors.gray.darker,
                style = BrandTheme.typography.subtitle3
            )
        }
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Frame",
            tint = BrandTheme.colors.gray.normal,
            modifier = Modifier.size(24.dp)
        )
    }
}

//@Preview(widthDp = 238, heightDp = 36)
@Composable
private fun PreviewProfileScreenItem() {
    MixedWashTheme {
        Box(Modifier.fillMaxSize()) {


            ProfileScreenItem(
                Modifier.align(Center),
                resource = Res.drawable.ic_clothes_hanger,
                text = "Order Hisotry"
            )
        }
    }
}