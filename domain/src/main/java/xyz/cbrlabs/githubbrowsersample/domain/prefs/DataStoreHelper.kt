package xyz.cbrlabs.githubbrowsersample.domain.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreHelper @Inject constructor(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    companion object {
        val KEY_LAST_UPDATED = longPreferencesKey("last_updated")
    }

    suspend fun saveLong(key: Preferences.Key<Long>, value: Long) {
        context.dataStore.edit { preferences -> preferences[key] = value }
    }

    fun getLong(key: Preferences.Key<Long>): Flow<Long?> = context.dataStore.data
        .map { preferences -> preferences[key] }

    suspend fun saveInt(key: Preferences.Key<Int>, value: Int) {
        context.dataStore.edit { preferences -> preferences[key] = value }
    }

    fun getInt(key: Preferences.Key<Int>): Flow<Int?> = context.dataStore.data
        .map { preferences -> preferences[key] }


    suspend fun saveString(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getString(key: Preferences.Key<String>): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[key] }

    suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getBoolean(key: Preferences.Key<Boolean>): Flow<Boolean?> = context.dataStore.data
        .map { preferences -> preferences[key] }

}
