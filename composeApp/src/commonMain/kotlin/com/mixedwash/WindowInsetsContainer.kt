package com.mixedwash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * A composable function that provides a container for content, applying specified window insets and handling system bar appearance.
 *
 * This function wraps its content within a [Box] and applies padding based on the provided [insetType].
 * It also manages the system status bar appearance (light or dark icons) via [SetStatusBarColor].
 *
 *
 * **Key Features:**
 *
 * *   **Inset Handling:** Automatically applies padding to the content based on the selected [insetType].
 *     This is crucial for ensuring your content isn't obscured by system UI elements like the status bar,
 *     navigation bar, or the software keyboard.
 * *   **System Bar Appearance:** Controls the light/dark appearance of the system status bar icons and text.
 * *   **Flexibility:** Offers various [insetType] options to fine-tune how content interacts with system in */
@Composable
fun WindowInsetsContainer(
    modifier: Modifier = Modifier,
    insetType: InsetType = InsetType.SafeDrawing,
    statusBarIconsLight: Boolean = true,
    imePadding: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {

    SetStatusBarColor(statusBarIconsLight)
    val paddingValues = insetType.toWindowInsets().asPaddingValues()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
            .imePadding()
        ,
        content = content
    )

}

fun Modifier.windowInsetsContainer(
    insetType: InsetType = InsetType.SafeDrawing,
    statusBarIconsLight: Boolean = true,
    imePadding: Boolean = true
) = composed {
    SetStatusBarColor(statusBarIconsLight)
    val paddingValues = insetType.toWindowInsets().asPaddingValues()
    this.fillMaxSize()
        .padding(paddingValues)
        .consumeWindowInsets(paddingValues)
        .apply { if (imePadding) this.imePadding() }

}

enum class InsetType {
    SafeDrawing, SafeContent, SafeGestures;

    @Composable
    fun toWindowInsets(): WindowInsets {
        return when (this) {
            SafeDrawing -> WindowInsets.safeDrawing
            SafeContent -> WindowInsets.safeContent
            SafeGestures -> WindowInsets.safeGestures
        }
    }
}

@Composable
expect fun SetStatusBarColor(isLight: Boolean)