package com.mixedwash.core.presentation.navigation

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.mixedwash.core.presentation.util.Logger

actual class AppCloser(application: Application) {
    private var currentActivity: Activity? = null

    init {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            // Implement other callbacks as needed
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (currentActivity == activity) {
                    currentActivity = null
                }
            }
        })
    }


    // TODO : FIX ME
    actual fun closeApp() {
        currentActivity?.finish() ?: Logger.d("TAG", "OOOOOO")
    }
}