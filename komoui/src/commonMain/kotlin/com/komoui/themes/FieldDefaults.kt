package com.komoui.themes

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shared sizing defaults for form-field components (Input, Select, Combobox, DatePicker) so their
 * heights stay consistent. Override per call site where a component exposes it.
 */
object KomoFieldDefaults {
    /** Minimum interactive height of a single-line form field. */
    val Height: Dp = 48.dp
}
