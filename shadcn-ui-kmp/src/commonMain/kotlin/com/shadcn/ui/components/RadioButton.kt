package com.shadcn.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.styles

/**
 * A Jetpack Compose Radio Group component inspired by Shadcn UI.
 * It manages the selection state for a group of [RadioButtonWithLabel]s.
 *
 * @param selectedValue The currently selected value in the group.
 * @param onValueChange Callback invoked when the selection changes. Provides the new selected value.
 * @param modifier The modifier to be applied to the radio group container.
 * @param content The composable content representing the radio buttons and their labels.
 * Each radio button should be wrapped in a selectable row with its label.
 * @param orientation The orientation of the radio group (horizontal or vertical).
 */
@Composable
fun <T> RadioGroup(
    selectedValue: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    orientation: LayoutOrientation = LayoutOrientation.Vertical, // New parameter for orientation
    content: @Composable () -> Unit
) {
    when (orientation) {
        LayoutOrientation.Vertical -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
        LayoutOrientation.Horizontal -> Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
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
 * styled to match Shadcn UI. This should be used as a child within a [RadioGroup].
 *
 * @param value The value associated with this radio button.
 * @param label The text label for this radio button.
 * @param selectedValue The currently selected value of the parent group.
 * @param onValueChange The callback for when this radio button is selected.
 * @param modifier The modifier to be applied to the row containing the radio button and label.
 * @param enabled Controls the enabled state of the radio button and label.
 * @param colors Optional custom colors for the radio button and label.
 */
@Composable
fun <T> RadioButtonWithLabel(
    value: T,
    label: String,
    selectedValue: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors? = null
) {
    val themeColors = MaterialTheme.styles
    val isSelected = (selectedValue == value)

    Row(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = { onValueChange(value) },
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            enabled = enabled,
            colors = colors ?: RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.styles.primary,
                unselectedColor = MaterialTheme.styles.input,
                disabledSelectedColor = MaterialTheme.styles.primary.copy(alpha = 0.5f),
                disabledUnselectedColor = MaterialTheme.styles.mutedForeground.copy(alpha = 0.5f)
            ),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = TextStyle(
                color = if (enabled) themeColors.foreground else themeColors.mutedForeground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
