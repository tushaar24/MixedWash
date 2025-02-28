package com.mixedwash

import android.app.Application
import com.mixedwash.core.data.util.AppCoroutineScope
import com.mixedwash.libs.loki.permission.internal.context.LokiService
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MixedWashApplication : Application() {
    val appCoroutineScope: AppCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        LokiService.initialize(context = applicationContext)

        initKoin {
            androidLogger()
            androidContext(this@MixedWashApplication)
        }

        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = "241995325784-opejcpg0g2bu25oj04e14skpmhabf2nd.apps.googleusercontent.com"
            )
        )


    }
}