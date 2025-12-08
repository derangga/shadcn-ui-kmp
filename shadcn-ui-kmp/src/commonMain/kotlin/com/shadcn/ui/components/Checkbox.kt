package com.shadcn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

/**
 * A Jetpack Compose Checkbox component inspired by Shadcn UI.
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
    
    // Animate background color
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledColors
            checked -> colors.checkedColors
            else -> colors.uncheckedColors
        },
        animationSpec = tween(durationMillis = 100), label = "checkboxBackgroundColor"
    )

    // Animate border color
    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledColors
            checked -> colors.checkedBorderColors
            else -> colors.unCheckedBorderColors
        },
        animationSpec = tween(durationMillis = 100), label = "checkboxBorderColor"
    )

    // Animate checkmark color
    val checkmarkColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledCheckMarkColor
            checked -> colors.checkedCheckmarkColor
            else -> colors.uncheckedCheckmarkColor
        },
        animationSpec = tween(durationMillis = 100), label = "checkboxCheckmarkColor"
    )

    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor = if (isPressed && enabled) {
        MaterialTheme.styles.ring
    } else borderColor

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(radius.sm))
            .background(backgroundColor)
            .border(1.dp, currentBorderColor, RoundedCornerShape(radius.sm))
            .toggleable(
                value = checked,
                onValueChange = { newChecked -> onCheckedChange?.invoke(newChecked) },
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = checkmarkColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

data class CheckboxColors(
    val checkedColors: Color,
    val uncheckedColors: Color,
    val disabledColors: Color,
    val disabledCheckMarkColor: Color,
    val checkedCheckmarkColor: Color,
    val uncheckedCheckmarkColor: Color,
    val checkedBorderColors: Color,
    val unCheckedBorderColors: Color,
)

object CheckboxDefaults {
    @Composable
    fun colors(): CheckboxColors {
        val styles = MaterialTheme.styles
        return CheckboxColors(
            styles.primary,
            Color.Transparent,
            styles.muted,
            styles.foreground,
            styles.primaryForeground,
            Color.Transparent,
            styles.primary,
            styles.input
        )
    }
}