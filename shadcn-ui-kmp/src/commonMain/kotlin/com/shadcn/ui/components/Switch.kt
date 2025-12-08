package com.shadcn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.shadcn.ui.themes.styles

/**
 * A Jetpack Compose Switch component inspired by Shadcn UI.
 * Provides a toggle switch for boolean states.
 *
 * @param checked Whether this switch is checked.
 * @param onCheckedChange Callback invoked when the switch's checked state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Controls the enabled state of the switch.
 * @param colors [SwitchStyle] that will be used to resolve the colors used for this switch in
 */
@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchStyle = SwitchDefaults.colors()
) {
    val density = LocalDensity.current

    val switchWidth = 40.dp
    val switchHeight = 18.dp
    val thumbSize = 16.dp
    val borderWidth = 1.dp

    val thumbOffset by animateFloatAsState(
        targetValue = with(density) {
            if (checked) (switchWidth - thumbSize - borderWidth * 2).toPx() else borderWidth.toPx()
        },
        animationSpec = tween(durationMillis = 150), label = "thumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) colors.checkedTrack else colors.uncheckedTrack,
        animationSpec = tween(durationMillis = 150), label = "trackColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (checked) colors.checkedBorder else colors.uncheckedBorder,
        animationSpec = tween(durationMillis = 150), label = "borderColor"
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) colors.checkedThumb else colors.uncheckedThumb,
        animationSpec = tween(durationMillis = 150), label = "thumbColor"
    )

    val disabledAlpha = 0.5f // Alpha for disabled state
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(width = switchWidth, height = switchHeight)
            .border(1.dp, borderColor.copy(alpha = if (enabled) 1f else disabledAlpha), CircleShape)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange ?: { /* do nothing if null */ },
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null
            )
            .drawBehind {
                val trackCornerRadius = CornerRadius(switchHeight.toPx() / 2f)
                val thumbRadius = thumbSize.toPx() / 2f

                // Draw track
                drawRoundRect(
                    color = trackColor.copy(alpha = if (enabled) 1f else disabledAlpha),
                    size = Size(size.width, size.height),
                    cornerRadius = trackCornerRadius
                )

                // Draw thumb
                drawCircle(
                    color = thumbColor.copy(alpha = if (enabled) 1f else disabledAlpha),
                    radius = thumbRadius,
                    center = Offset(
                        x = thumbOffset + thumbRadius,
                        y = size.height / 2f
                    )
                )
            }
    )
}

data class SwitchStyle(
    val checkedBorder: Color,
    val checkedTrack: Color,
    val checkedThumb: Color,
    val uncheckedBorder: Color,
    val uncheckedTrack: Color,
    val uncheckedThumb: Color,
)

object SwitchDefaults {
    @Composable
    fun colors(): SwitchStyle {
        val styles = MaterialTheme.styles
        return SwitchStyle(
            checkedBorder = styles.primary,
            checkedTrack = styles.primary,
            checkedThumb = styles.primaryForeground,
            uncheckedBorder = styles.input,
            uncheckedTrack = styles.input,
            uncheckedThumb = styles.primary
        )
    }
}
