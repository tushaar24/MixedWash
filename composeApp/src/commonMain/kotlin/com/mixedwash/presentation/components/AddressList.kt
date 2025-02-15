package com.mixedwash.presentation.components

import BrandTheme
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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mixedwash.cardSpacing
import com.mixedwash.features.createOrder.presentation.models.Address
import com.mixedwash.features.createOrder.presentation.models.AddressId
import com.mixedwash.screenHorizontalPadding
import com.mixedwash.screenVerticalPadding
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.defaults.IconButton

@Composable
fun AddressList(
    modifier: Modifier = Modifier,
    addresses: List<Address>,
    selectedIndex: Int = -1,
    onAddressClicked: ((Int) -> Unit)? = null,
    onAddressEdit: ((Address) -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(cardSpacing)
    ) {
        items(count = addresses.size,
            key = { index -> addresses[index].id.value!! }) { index ->

            val address = addresses[index]

            val borderColor = if (selectedIndex == index) Color.Transparent
            else Color.Transparent

            val containerColor = if (selectedIndex == index) BrandTheme.colors.gray.c300
            else Color.Transparent

            Row(modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp, color = borderColor, shape = BrandTheme.shapes.card
                )
                .clip(BrandTheme.shapes.card)
                .background(containerColor)
                .clickable(enabled = onAddressClicked != null && selectedIndex != index) {
                    onAddressClicked!!(
                        index
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
                Column(modifier = Modifier) {
                    Text(
                        text = address.title,
                        style = BrandTheme.typography.subtitle2,
                        color = BrandTheme.colors.gray.dark
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = address.run {
                            """
                                    $addressLine1, $addressLine2
                                    $addressLine3, $pinCode
                                """.trimIndent()
                        },
                        style = BrandTheme.typography.body2,
                    )

                }
                Spacer(modifier = Modifier.weight(1f))

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
            id = AddressId("alkka"),
            title = "Home",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            id = AddressId("lsasd"),
            title = "Office",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            id = AddressId("lkmk"),
            title = "Dadi",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        )
    )
    var selectedIndex by remember { mutableIntStateOf(0) }

    MixedWashTheme {
        AddressList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenHorizontalPadding, vertical = screenVerticalPadding),
            addresses = addresses,
            selectedIndex = selectedIndex, onAddressClicked = { selectedIndex = it },
            onAddressEdit = {}

        )
    }
}