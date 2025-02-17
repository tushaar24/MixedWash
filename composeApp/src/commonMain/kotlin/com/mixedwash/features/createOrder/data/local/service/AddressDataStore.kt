package com.mixedwash.features.createOrder.data.local.service

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull

class AddressDataStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val ADDRESS_KEY = stringPreferencesKey("address")
    }

    suspend fun put(uid: String) {
         try {
            dataStore.edit { preferences ->
                preferences[ADDRESS_KEY] = uid
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun get(): String? =
        dataStore.data.singleOrNull().let { preferences -> preferences?.get(ADDRESS_KEY) }

    suspend fun getFlow(): Flow<String?> = dataStore.data
        .catch { e -> e.printStackTrace() ; flowOf(null) }
        .map { preferences -> preferences[ADDRESS_KEY] }

}