package com.mixedwash.core.presentation.components

import androidx.compose.ui.graphics.NativePaint
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter

actual fun NativePaint.setMaskFilter(blurRadius: Float) {
    // Divide by 2 to accommodate for the different interpretation of blur radius in Skia
    this.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, blurRadius / 2, true)
}
