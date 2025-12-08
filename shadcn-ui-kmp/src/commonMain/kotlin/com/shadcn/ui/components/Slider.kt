package com.shadcn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Slider as ComposeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

/**
 * A Jetpack Compose Slider component inspired by Shadcn UI.
 * Allows users to select a value from a continuous range.
 *
 * @param value The current value of the slider.
 * @param onValueChange Callback invoked when the slider's value changes.
 * @param modifier The modifier to be applied to the slider.
 * @param valueRange The range of values the slider can take.
 * @param steps The number of discrete steps the slider can take.
 * @param enabled Controls the enabled state of the slider.
 * @param colors Optional custom colors for the slider.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    colors: SliderColors? = null
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    ComposeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
        valueRange = valueRange,
        steps = steps,
        enabled = enabled,
        colors = colors ?: SliderDefaults.colors(
            thumbColor = themeColors.background,
            activeTrackColor = themeColors.primary,
            inactiveTrackColor = themeColors.secondary,
            activeTickColor = themeColors.primaryForeground.copy(alpha = 0.5f),
            inactiveTickColor = themeColors.secondaryForeground.copy(alpha = 0.5f),
            disabledThumbColor = themeColors.mutedForeground.copy(alpha = 0.5f),
            disabledActiveTrackColor = themeColors.primary.copy(alpha = 0.5f),
            disabledInactiveTrackColor = themeColors.secondary.copy(alpha = 0.5f),
            disabledActiveTickColor = themeColors.primaryForeground.copy(alpha = 0.2f),
            disabledInactiveTickColor = themeColors.secondaryForeground.copy(alpha = 0.2f)
        ),
        thumb = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(themeColors.background)
                    .border(2.dp, themeColors.primary, CircleShape)
            )
        },
        track = { sliderPositions ->
            val trackColor = if (enabled) themeColors.secondary else themeColors.secondary.copy(alpha = 0.5f)
            val activeTrackColor = if (enabled) themeColors.primary else themeColors.primary.copy(alpha = 0.5f)
            // Calculate the fraction of the active track based on value and valueRange
            val activeTrackWidthFraction = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(radius.full))
                    .background(trackColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(activeTrackWidthFraction)
                        .height(8.dp)
                        .clip(RoundedCornerShape(radius.full))
                        .background(activeTrackColor)
                )
            }
        }
    )
}
