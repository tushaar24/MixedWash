package com.mixedwash.core.presentation.components

import android.graphics.BlurMaskFilter
import androidx.compose.ui.graphics.NativePaint

actual fun NativePaint.setMaskFilter(blurRadius: Float) {
    this.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
}
