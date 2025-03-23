package com.mixedwash.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.dump.SmallButton
import com.mixedwash.core.presentation.components.gradient
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.model.HomeBanner

@Composable
fun HomeBanner(
    banner: HomeBanner,
    statusBarHeight: Dp,
    onBannerButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = Modifier.height(statusBarHeight + 300.dp)) {
        Box(
            modifier = modifier.matchParentSize()
                .gradient(gradient = banner.gradient)
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val contentColor = Color.parse(banner.contentTextColor)

            Spacer(Modifier.height(statusBarHeight))

            Row(
                modifier = Modifier.padding(top = 30.5.dp).fillMaxWidth()
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(end = 16.dp)
                ) {

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = banner.heading,
                        color = contentColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )

                    Text(
                        text = banner.description,
                        fontSize = 12.sp,
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

