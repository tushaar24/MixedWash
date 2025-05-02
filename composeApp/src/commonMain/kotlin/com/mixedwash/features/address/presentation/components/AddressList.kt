package com.mixedwash.features.address.presentation.components

import BrandTheme
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.address.presentation.AddressSearchEvent
import com.mixedwash.features.address.presentation.AddressSearchState
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.components.IconButton
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.screenVerticalPadding
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.new_address_drawing
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddressList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    isLoading: Boolean = false,
    addresses: List<Address>,
    onSearchBoxClick: (() -> Unit)? = null,
    selectedAddressId: String? = null,
    onAddressClicked: ((String) -> Unit)? = null,
    onAddressEdit: ((Address) -> Unit)? = null,
    addressSearchState: AddressSearchState
) {
    val searchBoxFocusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = modifier.animateContentSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Box(
                modifier =
                    Modifier.fillMaxWidth().then(
                        if (onSearchBoxClick != null) {
                        Modifier.noRippleClickable {
                            onSearchBoxClick()
                        }
                    } else Modifier)
            ) {
                AddressSearch(
                    modifier = Modifier.fillMaxWidth().focusRequester(searchBoxFocusRequester),
                    query = addressSearchState.query,
                    placeHolder = addressSearchState.placeHolder,
                    enabled = addressSearchState.enabled && onSearchBoxClick == null && !isLoading,
                    places = addressSearchState.autocompleteResult,
                    onValueChange = {
                        addressSearchState.onEvent(
                            AddressSearchEvent.OnValueChange(
                                it
                            )
                        )
                    },
                    onLocationClick = { addressSearchState.onEvent(AddressSearchEvent.OnLocationClick) },
                    onPlaceSelected = {
                        addressSearchState.onEvent(
                            AddressSearchEvent.OnPlaceSelected(
                                it
                            )
                        )
                    },
                    onClearRequest = { addressSearchState.onEvent(AddressSearchEvent.OnClear) },
                    fetchingCurrentLocation = addressSearchState.fetchingLocation,
                    searchBoxFocusRequester = searchBoxFocusRequester
                )
            }
        }

        item {
            Image(
                modifier = Modifier.noRippleClickable(enabled = addressSearchState.enabled && !isLoading) {
                    try {
                        searchBoxFocusRequester.requestFocus()
                    } catch (e: Exception) {
                        Logger.e("TAG", "Failed to request focus: ${e.message}")
                    }
                }.padding(vertical = 8.dp),
                painter = painterResource(Res.drawable.new_address_drawing),
                contentDescription = "Add Address Icon"
            )
        }

        if (addresses.isEmpty()) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "No Saved Addresses",
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(count = addresses.size, key = { index -> addresses[index].uid }) { index ->

                val address = addresses[index]
                val isSelected = selectedAddressId == address.uid

                val borderColor = if (isSelected) Color.Transparent
                else Color.Transparent

                val containerColor = if (isSelected) BrandTheme.colors.gray.c300
                else Color.Transparent

                Row(
                    modifier = Modifier.fillMaxWidth().animateItem().border(
                        1.dp, color = borderColor, shape = BrandTheme.shapes.card
                    ).clip(BrandTheme.shapes.card).background(containerColor)
                        .clickable(enabled = onAddressClicked != null && !isSelected) {
                            onAddressClicked!!(
                                address.uid
                            )
                        }.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = "Location Icon",
                        tint = BrandTheme.colors.gray.dark,
                        modifier = Modifier.padding(top = 4.dp, end = 18.dp).height(16.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = address.title,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.SemiBold,
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

            item {
                Spacer(Modifier.height(100.dp))
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

    var selectedId by remember { mutableStateOf("") }
    MixedWashTheme {
        AddressList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenHorizontalPadding, vertical = screenVerticalPadding),
            addresses = addresses,
            selectedAddressId = selectedId, onAddressClicked = { selectedId = it },
            onAddressEdit = {},
            addressSearchState = AddressSearchState.initialState(),
            listState = rememberLazyListState()
        )
    }
}