package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_add
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AddItemButton(
    subItems: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(BrandTheme.shapes.button)
            .clickable(onClick = onClick)
            .background(BrandTheme.colors.gray.darker)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            Alignment.CenterHorizontally
        )
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_add),
            contentDescription = null,
            tint = BrandTheme.colors.gray.lighter
        )

        Text(
            text = if (subItems) "Add Items" else "Add",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BrandTheme.colors.gray.lighter
        )
    }

}