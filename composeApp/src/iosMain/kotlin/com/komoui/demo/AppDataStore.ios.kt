package com.komoui.demo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.komoui.demo.themes.AppPreferences
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createDataStore(): AppPreferences = DataStoreAppPreferences(
    PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            (requireNotNull(documentDirectory).path + "/$DATA_STORE_FILE_NAME").toPath()
        }
    )
)

private class DataStoreAppPreferences(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {
    override val theme: Flow<String> = dataStore.data.map {
        it[stringPreferencesKey("theme")] ?: "system"
    }

    override val styles: Flow<String> = dataStore.data.map {
        it[stringPreferencesKey("styles")] ?: "Default"
    }

    override suspend fun setTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("theme")] = theme
        }
    }

    override suspend fun setStyles(styles: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("styles")] = styles
        }
    }
}
