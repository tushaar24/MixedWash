package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
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
import com.mixedwash.core.presentation.components.clearFocusOnKeyboardDismiss

@Composable
fun DefaultSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    placeHolder: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .clip(BrandTheme.shapes.textField)
            .border(
                width = 0.5.dp,
                color = BrandTheme.colors.gray.c300,
                shape = BrandTheme.shapes.textField
            ).background(BrandTheme.colors.gray.c100)
            .padding(horizontal = 16.dp),

        verticalAlignment = Alignment.CenterVertically,
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
    }
}
