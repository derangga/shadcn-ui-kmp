package dr.shadcn.kmp.pages.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dr.shadcn.kmp.themes.ThemeEvent
import dr.shadcn.kmp.themes.ThemeProvider
import dr.shadcn.kmp.themes.getStyles
import dr.shadcn.kmp.themes.setStyles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThemesViewModel(private val prefs: DataStore<Preferences>) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        viewModelScope.launch {
            val initialStyle = prefs.getStyles().first()
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