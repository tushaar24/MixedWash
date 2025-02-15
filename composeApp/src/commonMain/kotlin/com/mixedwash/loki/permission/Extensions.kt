package com.mixedwash.loki.permission

/**
 * Opens the settings for the app.
 *
 * This is useful for directing the user to the app settings to enable permissions after they have
 * been denied permanently.
 */
public fun LocationPermissionController.Companion.openSettings() {
    openPermissionSettings()
}

internal expect fun openPermissionSettings()