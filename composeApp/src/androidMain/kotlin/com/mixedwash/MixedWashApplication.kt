package com.mixedwash

import android.app.Application
import com.mixedwash.services.loki.permission.internal.context.LokiService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MixedWashApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        LokiService.initialize(context = applicationContext)

        initKoin {
            androidLogger()
            androidContext(this@MixedWashApplication)
        }

    }
}