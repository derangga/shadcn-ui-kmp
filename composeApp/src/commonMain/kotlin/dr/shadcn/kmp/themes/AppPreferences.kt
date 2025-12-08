package dr.shadcn.kmp.themes

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

suspend fun DataStore<Preferences>.setTheme(theme: String) {
    edit { dataStore ->
        val key = stringPreferencesKey("theme")
        dataStore[key] = theme
    }
}

fun DataStore<Preferences>.getTheme(): Flow<String> {
    return data.map {
        val key = stringPreferencesKey("theme")
        it[key] ?: "system"
    }
}

suspend fun DataStore<Preferences>.setStyles(styles: String) {
    edit { dataStore ->
        val key = stringPreferencesKey("styles")
        dataStore[key] = styles
    }
}

fun DataStore<Preferences>.getStyles(): Flow<String> {
    return data.map {
        val key = stringPreferencesKey("styles")
        it[key] ?: "Default"
    }
}