package com.mixedwash.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.dump.SmallButton
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.model.HomeBanner

@Composable
fun HomeBanner(
    banner: HomeBanner,
    address: Address?,
    onBannerButtonClicked: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFaqs: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = Modifier.height(300.dp)) {
        Box(
            modifier = modifier.matchParentSize()
                .gradient(gradient = banner.gradient)
        )

        Column(
            modifier = Modifier.statusBarsPadding().fillMaxWidth()
        ) {
            HomeTopBar(
                addressTitle = address?.title ?: "",
                addressLine = address?.pinCode ?: "",
                onExpand = {},
                onProfileClick = onNavigateToProfile,
                onFAQsClick = onNavigateToFaqs,
                contentColor = Color.parse(banner.uiTextColor)
            )


            val contentColor = Color.parse(banner.contentTextColor)
            Row(
                modifier = Modifier.padding(top = 30.5.dp).fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.height(276.dp)) {
                    Logger.d("TAG", banner.imageUrl)
                    AsyncImage(
                        model = banner.imageUrl,
                        modifier = Modifier.height(160.dp).width(200.dp)
                            .align(Alignment.BottomStart).offset(y = 6.dp),
                        contentDescription = null,
                    )
                }

                Column(
                    modifier = Modifier.defaultMinSize(minWidth = 189.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = banner.heading,
                        color = contentColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )

                    Text(
                        text = banner.description,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = Color.parse(banner.contentTextColor)
                    )

                    banner.button?.let {
                        SmallButton(
                            onClick = onBannerButtonClicked,
                            text = it,
                            contentColor = contentColor,
                            borderColor = contentColor
                        )
                    }
                }

            }
        }
    }
}

