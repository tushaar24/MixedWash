package com.mixedwash.presentation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mixedwash.ui.theme.Gray900
import com.mixedwash.ui.theme.GreenDark
import com.mixedwash.ui.theme.RedDark
import com.mixedwash.ui.theme.Yellow

enum class SnackBarType(
    val contentColor: Color,
    val icon: ImageVector,
) {
    SUCCESS(
        contentColor = GreenDark,
        icon = Icons.Rounded.CheckCircle,
    ),
    WARNING(
        contentColor = Yellow,
        icon = Icons.Rounded.Warning,
    ),
    ERROR(
        contentColor = RedDark,
        icon = Icons.Rounded.Close,
    ),
    INFO(
        contentColor = Gray900,
        icon = Icons.Default.Info,
    )
    ;

    companion object {
        fun fromString(value: String): SnackBarType? {
            return entries.find {  it.name == value }
        }
    }
}

/**
 * Encodes the snackbar type with the message, to be decoded by the [decodeSnackBar]
 * function, when displaying the snackbar.
 *
 * @param type
 * */
fun String.withSnackbarType(type: SnackBarType): String {
    return "${type.name}|$this"
}

/**
 * Decodes a snackbar message encoded with [withSnackbarType]. For messages without any snackbartype it returns
 * `null` as the [SnackBarType]
 * */
fun decodeSnackBar(encodedString: String): Pair<SnackBarType?, String> {
    val parts = encodedString.split("|", limit = 2)
    return if (parts.size == 2) {
        val type = SnackBarType.fromString(parts[0])
        type to parts[1]
    } else {
        null to encodedString // Default case: no type, just a plain message
    }
}