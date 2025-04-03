package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.features.services.presentation.model.ServiceDetailPresentation


@Composable
fun DetailsList(modifier: Modifier = Modifier, details: List<ServiceDetailPresentation>) {

    Column(modifier = modifier.fillMaxWidth().animateContentSize()) {
        Text(
            text = "Details",
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp,
            fontSize = 16.sp,
            color = BrandTheme.colors.gray.normalDark
        )
        Spacer(Modifier.height(16.dp))
        details.forEach { detail ->
            DetailParameter(key = detail.key, value = detail.value)
        }
    }
}
