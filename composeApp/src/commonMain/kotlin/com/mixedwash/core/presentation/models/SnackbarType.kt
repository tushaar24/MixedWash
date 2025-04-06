package com.mixedwash.core.presentation.models

import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_snackbar_error
import mixedwash.composeapp.generated.resources.ic_snackbar_success
import mixedwash.composeapp.generated.resources.ic_snackbar_warning
import org.jetbrains.compose.resources.DrawableResource

enum class SnackBarType(
    val icon: DrawableResource,
) {
    SUCCESS(
        icon = Res.drawable.ic_snackbar_success,
    ),
    WARNING(
        icon = Res.drawable.ic_snackbar_warning,
    ),
    ERROR(
        icon = Res.drawable.ic_snackbar_error,
    ),
    DEFAULT(
        icon = Res.drawable.ic_snackbar_warning,
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