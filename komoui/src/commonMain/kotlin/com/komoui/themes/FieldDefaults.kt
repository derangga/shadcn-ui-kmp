package com.komoui.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Colors for the trigger of a single-line form field (Select, Combobox, DatePicker). These share
 * one shape because they render the same bordered box; the dropdown/popup surfaces stay themed via
 * [KomoStyles] (popover tokens).
 *
 * @property text Color of the selected/entered value.
 * @property placeholder Color of the placeholder shown when there is no value.
 * @property border Border color at rest.
 * @property focusedBorder Border color while focused/open.
 * @property errorBorder Border color when the field is in error.
 * @property supportingText Supporting text color.
 * @property errorSupportingText Supporting text color when the field is in error.
 */
data class FieldColors(
    val text: Color,
    val placeholder: Color,
    val border: Color,
    val focusedBorder: Color,
    val errorBorder: Color,
    val supportingText: Color,
    val errorSupportingText: Color,
)

/**
 * Shared sizing and color defaults for form-field components (Input, Select, Combobox, DatePicker)
 * so their heights and colors stay consistent. Override per call site via [colors].
 */
object KomoFieldDefaults {
    /** Minimum interactive height of a single-line form field. */
    val Height: Dp = 48.dp

    private fun colorsFrom(styles: KomoStyles): FieldColors = FieldColors(
        text = styles.foreground,
        placeholder = styles.mutedForeground,
        border = styles.border,
        focusedBorder = styles.ring,
        errorBorder = styles.destructive,
        supportingText = styles.mutedForeground,
        errorSupportingText = styles.destructive,
    )

    /** [FieldColors] with the default KomoUI color scheme. */
    @Composable
    fun colors(): FieldColors = colorsFrom(MaterialTheme.styles)

    /** [FieldColors] with the default scheme, mutated by [overrides]. */
    @Composable
    fun colors(overrides: FieldColors.() -> FieldColors): FieldColors =
        colorsFrom(MaterialTheme.styles).overrides()
}
