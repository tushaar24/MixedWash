package com.mixedwash.libs.loki.permission

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

internal actual fun openPermissionSettings() {
    UIApplication.sharedApplication().openURL(
        url = NSURL(string = UIApplicationOpenSettingsURLString),
        options = emptyMap<Any?, Any>(),
        completionHandler = null
    )
}