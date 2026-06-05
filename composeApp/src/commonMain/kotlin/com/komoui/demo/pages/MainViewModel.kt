package com.komoui.demo.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komoui.demo.Content
import com.komoui.demo.HomeContent
import com.komoui.demo.themes.AppPreferences
import com.komoui.demo.themes.ThemeEvent
import com.komoui.demo.themes.ThemeProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MainViewModel(val prefs: AppPreferences): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<Content>>(emptyList())
    val searchResults: StateFlow<List<Content>> = _searchResults

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    val results = performSearch(query)
                    _searchResults.value = results
                }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun performSearch(query: String): List<Content> {
        return HomeContent.contents.filter { it.title.contains(query.trim(), ignoreCase = true) }
    }

    fun switchTheme(isDark: Boolean) {
        val newValue = if (isDark) {
            "light"
        } else {
            "dark"
        }

        viewModelScope.launch {
            prefs.setTheme(newValue)
            ThemeProvider.sendEvent(ThemeEvent.Theme(newValue))
        }
    }
}
