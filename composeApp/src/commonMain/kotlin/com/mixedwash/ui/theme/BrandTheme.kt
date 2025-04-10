import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.mixedwash.ui.theme.ColorScheme
import com.mixedwash.ui.theme.ColorScheme.Companion.lightScheme
import com.mixedwash.ui.theme.Shapes
import com.mixedwash.ui.theme.Typography
import com.mixedwash.ui.theme.Typography.Companion.Poppins
import com.mixedwash.ui.theme.Typography.Companion.defaultTypography

@Suppress("DEPRECATION_ERROR")
@Composable
fun BrandTheme(
    colorScheme: ColorScheme,
    shapes: Shapes,
    typography: Typography = defaultTypography(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        typography = MaterialTheme.typography.copy(
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = Poppins()  // Set font family here
            )
        )
    ) {

    }
    val rippleIndication = androidx.compose.material.ripple.rememberRipple()
    val selectionColors = rememberTextSelectionColors(colorScheme)
    CompositionLocalProvider(
        LocalBrandColors provides colorScheme,
        LocalIndication provides rippleIndication,
        LocalRippleTheme provides MaterialRippleTheme,
        LocalTextSelectionColors provides selectionColors,
        LocalShapes provides shapes,
        LocalTypography provides typography,
        LocalContentColor provides BrandTheme.colors.gray.dark,
    ) {
        ProvideTextStyle(value = typography.body1, content = content)
    }
}

private val LocalBrandColors = staticCompositionLocalOf { lightScheme }
private val LocalTypography =
    compositionLocalOf<Typography> { error("No BrandTypography provided") }
internal val LocalShapes = staticCompositionLocalOf { Shapes() }

@Suppress("DEPRECATION_ERROR")
@Immutable
private object MaterialRippleTheme : androidx.compose.material.ripple.RippleTheme {
    @Deprecated("Super method deprecated")
    @Composable
    override fun defaultColor() = LocalContentColor.current

    @Deprecated("Super method deprecated")
    @Composable
    override fun rippleAlpha() = DefaultRippleAlpha
}

private val DefaultRippleAlpha = RippleAlpha(
    pressedAlpha = StateTokens.PressedStateLayerOpacity,
    focusedAlpha = StateTokens.FocusStateLayerOpacity,
    draggedAlpha = StateTokens.DraggedStateLayerOpacity,
    hoveredAlpha = StateTokens.HoverStateLayerOpacity
)

internal object StateTokens {
    const val DraggedStateLayerOpacity = 0.16f
    const val FocusStateLayerOpacity = 0.12f
    const val HoverStateLayerOpacity = 0.08f
    const val PressedStateLayerOpacity = 0.12f
}


/**
 * Contains functions to access the current theme values provided at the call site's position in
 * the hierarchy.
 */
object BrandTheme {
    /**
     * Retrieves the current [ColorScheme] at the call site's position in the hierarchy.
     */
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalBrandColors.current

    /**
     * Retrieves the current [Typography] at the call site's position in the hierarchy.
     */
    val typography: com.mixedwash.ui.theme.Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

}

@Composable
/*@VisibleForTesting*/
internal fun rememberTextSelectionColors(colorScheme: ColorScheme): TextSelectionColors {
    val primary = colorScheme.primary.normalDark
    return remember(primary) {
        TextSelectionColors(
            handleColor = primary,
            backgroundColor = primary.copy(alpha = TextSelectionBackgroundOpacity),
        )
    }
}

/*@VisibleForTesting*/
internal const val TextSelectionBackgroundOpacity = 0.4f
