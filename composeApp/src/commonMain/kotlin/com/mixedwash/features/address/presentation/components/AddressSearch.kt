package com.mixedwash.features.address.presentation.components

import BrandTheme
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.core.presentation.components.clearFocusOnKeyboardDismiss
import com.mixedwash.libs.loki.autocomplete.AutocompletePlace
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_current_location
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSearch(
    modifier: Modifier = Modifier,
    query: String,
    placeHolder: String,
    enabled: Boolean,
    places: List<AutocompletePlace>,
    onValueChange: (String) -> Unit,
    onLocationClick: () -> Unit,
    onPlaceSelected: (AutocompletePlace) -> Unit,
    onClearRequest: () -> Unit,
    fetchingCurrentLocation: Boolean
) {

    Logger.d("TAG", "${places.forEach { "${it.address}\n" }}")
    val expanded by remember(places) { derivedStateOf { places.isNotEmpty() } }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {},
    ) {
        AddressSearchBar(
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
            onLocationClick = onLocationClick,
            onValueChange = onValueChange,
            query = query,
            placeHolder = placeHolder,
            enabled = enabled,
            fetchingCurrentLocation = fetchingCurrentLocation
        )

        ExposedDropdownMenu(
            modifier = Modifier.background(BrandTheme.colors.background)
                .animateContentSize(),
            shape = BrandTheme.shapes.textField,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            border = BorderStroke(width = 0.5.dp, color = BrandTheme.colors.gray.c300),
            matchTextFieldWidth = true,
            expanded = expanded,
            onDismissRequest = onClearRequest,
            scrollState = rememberScrollState(),
        ) {
            places.forEach { p ->
                DropdownMenuItem(
                    modifier = Modifier.padding(vertical = 8.dp),
                    onClick = {
                        onPlaceSelected(p)
                    },
                    text = {
                        Text(
                            text = p.address,
                            style = BrandTheme.typography.body2,
                            maxLines = 3,
                            minLines = 2,
                        )
                    })
            }

        }
    }

}


@Composable
fun AddressSearchBar(
    modifier: Modifier,
    query: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    onLocationClick: () -> Unit,
    enabled: Boolean,
    fetchingCurrentLocation: Boolean
) {

    val defaultColor = BrandTheme.colors.gray.normalDark
    val pulseColor = BrandTheme.colors.gray.darker

    // Determine the tint based on fetching state.
    val tint: Color = if (fetchingCurrentLocation) {
        // Use an infinite transition for the pulsing effect.
        val infiniteTransition = rememberInfiniteTransition()
        infiniteTransition.animateColor(
            initialValue = defaultColor,
            targetValue = pulseColor,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        ).value
    } else {
        // Animate back to the default color (gray) when not fetching.
        animateColorAsState(
            targetValue = defaultColor,
            animationSpec = tween(durationMillis = 500)
        ).value
    }


    Row(
        modifier = modifier.clip(BrandTheme.shapes.textField).border(
            width = 0.5.dp,
            color = BrandTheme.colors.gray.c300,
            shape = BrandTheme.shapes.textField
        ).height(52.dp).background(BrandTheme.colors.gray.c100).padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var hasFocus by remember { mutableStateOf(false) }
        BasicTextField(
            value = query.ifBlank { if (hasFocus) "" else placeHolder },
            onValueChange = onValueChange,
            modifier = Modifier.onFocusChanged { hasFocus = it.hasFocus }
                .clearFocusOnKeyboardDismiss().fillMaxWidth().weight(1f),
            maxLines = 1,
            minLines = 1,
            textStyle = BrandTheme.typography.body2.copy(color = if (query.isBlank()) BrandTheme.colors.gray.normalDark else Color.Unspecified),
            singleLine = true,
            enabled = enabled
        )

        Box(
            modifier = Modifier.clip(BrandTheme.shapes.chip)
                .clickable(enabled = enabled, onClick = onLocationClick).padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(Res.drawable.ic_current_location),
                contentDescription = null,
                tint = tint
            )
        }
    }

}
