package com.mixedwash.features.address.presentation.components

import BrandTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mixedwash.features.address.presentation.AddressSearchEvent
import com.mixedwash.features.address.presentation.AddressSearchState
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.cardSpacing
import com.mixedwash.ui.theme.components.IconButton
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.screenVerticalPadding

@Composable
fun AddressList(
    modifier: Modifier = Modifier,
    addresses: List<Address>,
    selectedAddressId: String? = null,
    onAddressClicked: ((String) -> Unit)? = null,
    onAddressEdit: ((Address) -> Unit)? = null,
    addressSearchState: AddressSearchState?=null
) {
    LazyColumn(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(cardSpacing)
    ) {
        item {
            addressSearchState?.let { state ->
                AddressSearch(
                    modifier = Modifier.fillMaxWidth(),
                    query = state.query,
                    placeHolder = state.placeHolder,
                    enabled = state.enabled,
                    places = state.autocompleteResult,
                    onValueChange = {state.onEvent(AddressSearchEvent.OnValueChange(it))},
                    onLocationClick = { state.onEvent(AddressSearchEvent.OnLocationClick) },
                    onPlaceSelected = { state.onEvent(AddressSearchEvent.OnPlaceSelected(it)) },
                    onClearRequest = { state.onEvent(AddressSearchEvent.OnClear) },
                    fetchingCurrentLocation = state.fetchingLocation,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        items(count = addresses.size, key = { index -> addresses[index].uid }) { index ->

            val address = addresses[index]
            val isSelected = selectedAddressId == address.uid

            val borderColor = if (isSelected) Color.Transparent
            else Color.Transparent

            val containerColor = if (isSelected) BrandTheme.colors.gray.c300
            else Color.Transparent

            Row(modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp, color = borderColor, shape = BrandTheme.shapes.card
                )
                .clip(BrandTheme.shapes.card)
                .background(containerColor)
                .clickable(enabled = onAddressClicked != null && !isSelected) {
                    onAddressClicked!!(
                        address.uid
                    )
                }
                .padding(horizontal = 16.dp, vertical = 16.dp)) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "Location Icon",
                    tint = BrandTheme.colors.gray.dark,
                    modifier = Modifier
                        .padding(top = 4.dp, end = 18.dp)
                        .size(14.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text (
                        text = address.title,
                        style = BrandTheme.typography.subtitle2,
                        color = BrandTheme.colors.gray.dark
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = address.run {
                            """
                                    $addressLine1, $addressLine2 $addressLine3 
                                    $pinCode
                                """.trimIndent()
                        },
                        style = BrandTheme.typography.body2,
                        color = BrandTheme.colors.gray.darker
                    )

                }
                Spacer(modifier = Modifier.padding(16.dp))

                onAddressEdit?.let {
                    IconButton(
                        iconSize = 16.dp,
                        imageVector = Icons.Rounded.MoreVert,
                        onClick = { it(address) },
                        enabled = true,
                        buttonColors = BrandTheme.colors.iconButtonColors()
                            .copy(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }

}


//@Preview(showSystemUi = true)
@Composable
private fun PreviewAddressList() {
    val addresses = listOf(
        Address(
            uid = "alkka",
            title = "Home",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            uid = "lsasd",
            title = "Office",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            uid = "lkmk",
            title = "Dadi",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        )
    )

    var selectedId by remember {mutableStateOf("")}
    MixedWashTheme {
        AddressList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenHorizontalPadding, vertical = screenVerticalPadding),
            addresses = addresses,
            selectedAddressId = selectedId, onAddressClicked = { selectedId = it },
            onAddressEdit = {},
            addressSearchState = AddressSearchState.initialState()
        )
    }
}