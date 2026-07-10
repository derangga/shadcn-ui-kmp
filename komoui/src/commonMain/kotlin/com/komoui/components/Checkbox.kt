package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.komoui.themes.radius
import com.komoui.themes.styles
import com.komoui.utils.komoToggleable

/**
 * A Jetpack Compose Checkbox component for KomoUI.
 *
 * @param checked Whether this checkbox is checked.
 * @param onCheckedChange Callback to be invoked when the checkbox's checked state changes.
 * @param modifier The modifier to be applied to the checkbox.
 * @param enabled Controls the enabled state of the checkbox. When `false`, this checkbox will not
 *      be interactable.
 * @param colors [CheckboxColors] that will be used to resolve the colors used for this checkbox in
 *      different states. See [CheckboxDefaults.colors].
 */
@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    val radius = MaterialTheme.radius

    // Resolve all colors in a single block
    val resolved = when {
        !enabled -> ResolvedCheckboxColors(
            background = colors.disabledColor,
            border = colors.disabledColor,
            checkmark = colors.disabledCheckmarkColor
        )
        checked -> ResolvedCheckboxColors(
            background = colors.checkedColor,
            border = colors.checkedBorderColor,
            checkmark = colors.checkedCheckmarkColor
        )
        else -> ResolvedCheckboxColors(
            background = colors.uncheckedColor,
            border = colors.uncheckedBorderColor,
            checkmark = colors.uncheckedCheckmarkColor
        )
    }

    val backgroundColor by animateColorAsState(
        targetValue = resolved.background,
        animationSpec = tween(durationMillis = 100), label = "checkboxBackgroundColor"
    )
    val borderColor by animateColorAsState(
        targetValue = resolved.border,
        animationSpec = tween(durationMillis = 100), label = "checkboxBorderColor"
    )
    val checkmarkColor by animateColorAsState(
        targetValue = resolved.checkmark,
        animationSpec = tween(durationMillis = 100), label = "checkboxCheckmarkColor"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor = if (isPressed && enabled) {
        MaterialTheme.styles.ring
    } else borderColor

    Box(
        modifier = modifier
            .komoToggleable(
                value = checked,
                onValueChange = { newChecked -> onCheckedChange?.invoke(newChecked) },
                enabled = enabled,
                role = Role.Checkbox,
                shape = RoundedCornerShape(radius.sm),
                interactionSource = interactionSource,
            )
            // Expands the touch target to 48.dp; the 24.dp visual stays (must follow min-size).
            .minimumInteractiveComponentSize()
            .size(24.dp)
            .clip(RoundedCornerShape(radius.sm))
            .background(backgroundColor)
            .border(1.dp, currentBorderColor, RoundedCornerShape(radius.sm)),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                // Null: the toggleable(role = Role.Checkbox) box already announces checked state.
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private data class ResolvedCheckboxColors(
    val background: Color,
    val border: Color,
    val checkmark: Color
)

/**
 * Holds the color values used by a [Checkbox] in different states.
 *
 * @param checkedColor Background color when checked.
 * @param uncheckedColor Background color when unchecked.
 * @param disabledColor Background color when disabled.
 * @param disabledCheckmarkColor Checkmark color when disabled.
 * @param checkedCheckmarkColor Checkmark color when checked.
 * @param uncheckedCheckmarkColor Checkmark color when unchecked.
 * @param checkedBorderColor Border color when checked.
 * @param uncheckedBorderColor Border color when unchecked.
 */
data class CheckboxColors(
    val checkedColor: Color,
    val uncheckedColor: Color,
    val disabledColor: Color,
    val disabledCheckmarkColor: Color,
    val checkedCheckmarkColor: Color,
    val uncheckedCheckmarkColor: Color,
    val checkedBorderColor: Color,
    val uncheckedBorderColor: Color,
)

/**
 * Contains the default values used by [Checkbox].
 */
object CheckboxDefaults {
    /**
     * Creates a [CheckboxColors] with the default KomoUI color scheme.
     *
     * @return A [CheckboxColors] instance using the current theme's color palette.
     */
    @Composable
    fun colors(): CheckboxColors {
        val styles = MaterialTheme.styles
        return CheckboxColors(
            checkedColor = styles.primary,
            uncheckedColor = Color.Transparent,
            disabledColor = styles.muted,
            disabledCheckmarkColor = styles.foreground,
            checkedCheckmarkColor = styles.primaryForeground,
            uncheckedCheckmarkColor = Color.Transparent,
            checkedBorderColor = styles.primary,
            uncheckedBorderColor = styles.input
        )
    }
}
