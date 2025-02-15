package com.mixedwash.loki.permission.internal.context

import android.annotation.SuppressLint
import android.content.Context
import com.mixedwash.loki.core.InternalLokiApi
import com.mixedwash.loki.permission.internal.activity.ActivityProvider


/**
 * Class for providing the application context.
 */
@InternalLokiApi
internal class ContextProvider(public val context: Context) {

    public companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: ContextProvider? = null

        @InternalLokiApi
        public fun create(context: Context): ContextProvider {
            if (instance == null) {
                instance = ContextProvider(context)
            }

            return instance!!
        }

        public fun getInstance(): ContextProvider = instance
            ?: throw IllegalStateException("ContextProvider has not been initialized")
    }
}

/**
 * This is a temporary work around. get the startup initializer classes working with the manifest.
 * */
object LokiService {
    @OptIn(InternalLokiApi::class)
    fun initialize(context: Context) {
        ContextProvider.create(context)
        ActivityProvider.create(context)
    }
}