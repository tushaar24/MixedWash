package com.mixedwash.core.presentation.components

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.ui.theme.dividerBlack
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_chat_filled
import mixedwash.composeapp.generated.resources.ic_chat_outlined
import mixedwash.composeapp.generated.resources.ic_cloth_hangar_filled
import mixedwash.composeapp.generated.resources.ic_cloth_hangar_outlined
import mixedwash.composeapp.generated.resources.ic_home_filled
import mixedwash.composeapp.generated.resources.ic_home_outlined
import mixedwash.composeapp.generated.resources.ic_profile_filled
import mixedwash.composeapp.generated.resources.ic_profile_outlined
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

enum class BottomBarItem(
    val id: Int,
    val iconOutlined: DrawableResource,
    val iconFilled: DrawableResource,
    val navigationRoute: Route,
) {
    HOME(0, Res.drawable.ic_home_outlined, Res.drawable.ic_home_filled, Route.HomeRoute),
    SERVICES(
        1,
        Res.drawable.ic_cloth_hangar_outlined,
        Res.drawable.ic_cloth_hangar_filled,
        Route.ServicesRoute(serviceId = null)
    ),
    SUPPORT(2, Res.drawable.ic_chat_outlined, Res.drawable.ic_chat_filled, Route.FaqRoute),
    PROFILE(
        3,
        Res.drawable.ic_profile_outlined,
        Res.drawable.ic_profile_filled,
        Route.ProfileRoute
    ),
}

@Composable
fun AppBottomBar(
    initialSelectedItem: BottomBarItem,
    onNavigate: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf(initialSelectedItem) }
    BottomAppBar(
        modifier = modifier,
        containerColor = BrandTheme.colors.gray.lighter,
    ) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 48.dp, top = 12.dp, end = 64.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomBarItem.entries.forEach { item ->
                    Box(modifier = Modifier.padding(12.dp).noRippleClickable {
                        selectedItem = item
                        onNavigate(item.navigationRoute)
                    }) {
                        Icon(
                            imageVector = vectorResource(if (item == selectedItem) item.iconFilled else item.iconOutlined),
                            contentDescription = null,
                            tint = if (item == selectedItem) BrandTheme.colors.gray.darker else BrandTheme.colors.gray.c400
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = dividerBlack, modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}