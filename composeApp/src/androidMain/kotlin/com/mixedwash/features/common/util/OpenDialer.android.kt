package com.mixedwash.features.common.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

actual class OpenDialer(
    private val context: Context,
) {

    actual fun open(phoneNo: String) {
        if (context !is Activity) {
            return
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        }

        val intent = Intent(Intent.ACTION_CALL).apply {
            data = "tel:+91${phoneNo}".toUri()
        }

        context.startActivity(intent)
    }
}