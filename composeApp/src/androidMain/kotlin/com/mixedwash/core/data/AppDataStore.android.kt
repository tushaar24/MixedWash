package com.mixedwash.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun createDataStore(context: Any?): DataStore<Preferences> = AppDataStore.getDataStore {
    require(value = context is Context, lazyMessage = { "Context Object is Required" })
    context.filesDir.resolve(dataStoreFileName).absolutePath

}