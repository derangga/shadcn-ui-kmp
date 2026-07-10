package com.komoui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.RadioButtonDefaults as M3RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.komoui.themes.KomoStyles
import com.komoui.themes.komoTypography
import com.komoui.themes.styles
import com.komoui.utils.komoSelectable

/**
 * Scope exposed to a [RadioGroup]'s content. Carries the group's current selection
 * so child [RadioButtonWithLabel]s stay in sync without re-passing selection state.
 */
class RadioGroupScope<T> internal constructor(
    val selectedValue: T,
    val onValueChange: (T) -> Unit
)

/**
 * A Jetpack Compose Radio Group component for KomoUI.
 * It owns the selection state for a group of [RadioButtonWithLabel]s and exposes it
 * to children via [RadioGroupScope]. The container is marked as a mutually-exclusive
 * selectable group for accessibility.
 *
 * @param selectedValue The currently selected value in the group.
 * @param onValueChange Callback invoked when the selection changes. Provides the new selected value.
 * @param modifier The modifier to be applied to the radio group container.
 * @param orientation The orientation of the radio group (horizontal or vertical).
 * @param content The radio buttons, typically [RadioButtonWithLabel]s, composed within [RadioGroupScope].
 */
@Composable
fun <T> RadioGroup(
    selectedValue: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    orientation: LayoutOrientation = LayoutOrientation.Vertical,
    content: @Composable RadioGroupScope<T>.() -> Unit
) {
    val scope = RadioGroupScope(selectedValue, onValueChange)
    when (orientation) {
        LayoutOrientation.Vertical -> Column(
            modifier = modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            scope.content()
        }
        LayoutOrientation.Horizontal -> Row(
            modifier = modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            scope.content()
        }
    }
}

/**
 * Helper enum for layout orientation.
 */
enum class LayoutOrientation {
    Vertical,
    Horizontal
}

/**
 * A convenience composable to combine a native [RadioButton] with a [Text] label,
 * with the KomoUI design tokens. Must be composed within a [RadioGroup]'s content,
 * from which it reads the current selection and reports changes.
 *
 * @param value The value associated with this radio button.
 * @param label The text label for this radio button.
 * @param modifier The modifier to be applied to the row containing the radio button and label.
 * @param enabled Controls the enabled state of the radio button and label.
 * @param colors The [RadioButtonStyle] for the radio button and label. See [RadioButtonDefaults.colors].
 */
@Composable
fun <T> RadioGroupScope<T>.RadioButtonWithLabel(
    value: T,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonStyle = RadioButtonDefaults.colors()
) {
    val isSelected = (selectedValue == value)

    Row(
        modifier = modifier
            .komoSelectable(
                selected = isSelected,
                onClick = { onValueChange(value) },
                enabled = enabled,
                role = Role.RadioButton,
            )
            .minimumInteractiveComponentSize()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            enabled = enabled,
            colors = M3RadioButtonDefaults.colors(
                selectedColor = colors.selected,
                unselectedColor = colors.unselected,
                disabledSelectedColor = colors.disabledSelected,
                disabledUnselectedColor = colors.disabledUnselected
            ),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.komoTypography.bodyMedium.copy(
                color = if (enabled) colors.label else colors.disabledLabel
            )
        )
    }
}

/** Colors used by a [RadioButtonWithLabel]. */
data class RadioButtonStyle(
    val selected: Color,
    val unselected: Color,
    val disabledSelected: Color,
    val disabledUnselected: Color,
    val label: Color,
    val disabledLabel: Color,
)

object RadioButtonDefaults {
    private fun colorsFrom(styles: KomoStyles): RadioButtonStyle = RadioButtonStyle(
        selected = styles.primary,
        unselected = styles.input,
        disabledSelected = styles.primary.copy(alpha = 0.5f),
        disabledUnselected = styles.mutedForeground.copy(alpha = 0.5f),
        label = styles.foreground,
        disabledLabel = styles.mutedForeground,
    )

    /** [RadioButtonStyle] with the default KomoUI color scheme. */
    @Composable
    fun colors(): RadioButtonStyle = colorsFrom(MaterialTheme.styles)

    /** [RadioButtonStyle] with the default scheme, mutated by [overrides]. */
    @Composable
    fun colors(overrides: RadioButtonStyle.() -> RadioButtonStyle): RadioButtonStyle =
        colorsFrom(MaterialTheme.styles).overrides()
}
