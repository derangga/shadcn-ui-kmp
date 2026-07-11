package com.komoui.demo.themes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AppPreferences {
    val theme: Flow<String>
    val styles: Flow<String>

    suspend fun setTheme(theme: String)

    suspend fun setStyles(styles: String)
}

class InMemoryAppPreferences : AppPreferences {
    private val themeState = MutableStateFlow("system")
    private val stylesState = MutableStateFlow("Default")

    override val theme: Flow<String> = themeState
    override val styles: Flow<String> = stylesState

    override suspend fun setTheme(theme: String) {
        themeState.value = theme
    }

    override suspend fun setStyles(styles: String) {
        stylesState.value = styles
    }
}
