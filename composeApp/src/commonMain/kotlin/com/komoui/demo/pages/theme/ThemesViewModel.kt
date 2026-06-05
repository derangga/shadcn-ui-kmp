package com.komoui.demo.pages.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komoui.demo.themes.AppPreferences
import com.komoui.demo.themes.ThemeEvent
import com.komoui.demo.themes.ThemeProvider
import com.komoui.demo.themes.getStyles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThemesViewModel(private val prefs: AppPreferences) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        viewModelScope.launch {
            val initialStyle = prefs.styles.first()
            _uiState.value = _uiState.value.copy(selected = initialStyle)
        }
    }

    fun showConfirmation(styles: String) {
        _uiState.value = _uiState.value.copy(pendingSelected = styles, showDialog = true)
    }

    fun confirmSelection(styles: String) {
        _uiState.value = UIState(selected = styles, pendingSelected = "", showDialog = false)
        viewModelScope.launch {
            prefs.setStyles(styles)
            ThemeProvider.sendEvent(ThemeEvent.Styles(
                styles,
                getStyles(styles)
            ))
        }
    }

    fun cancelConfirmation() {
        _uiState.value = _uiState.value.copy(pendingSelected = "", showDialog = false)
    }

    data class UIState(
        val selected: String = "Default",
        val pendingSelected: String = "",
        val showDialog: Boolean = false,
    )
}
