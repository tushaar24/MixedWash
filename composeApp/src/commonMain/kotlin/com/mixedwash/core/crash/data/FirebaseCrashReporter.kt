package com.mixedwash.core.crash.data

import com.mixedwash.core.crash.domain.CrashReporter
import com.mixedwash.core.data.util.AppCoroutineScope
import com.mixedwash.core.domain.config.AppConfig
import dev.gitlive.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FirebaseCrashReporter(
    private val crashlytics: FirebaseCrashlytics,
    userIdentifier: Flow<String>,
    appConfig: AppConfig,
    appCoroutineScope: AppCoroutineScope
) : CrashReporter {

    init {
        crashlytics.setCrashlyticsCollectionEnabled(true)
        appCoroutineScope.launch {
            userIdentifier.collect {
                crashlytics.setUserId(it)
            }
        }
        crashlytics.setCustomKeys(
            mapOf(
                "version" to appConfig.versionString,
                "usingStagingOrdersService" to appConfig.useStagingOrdersService,
                "bypassLocationCheckEnabled" to appConfig.bypassLocationCheck
            )
        )
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun recordException(throwable: Throwable, message: String) {
        crashlytics.setCustomKey("message", message)
        crashlytics.recordException(throwable)
    }

    override fun recordException(throwable: Throwable, keys: Map<String, Any>?) {
        keys?.let{ k -> crashlytics.setCustomKeys(k) }
        crashlytics.recordException(throwable)
    }

}