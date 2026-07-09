package com.komoui.components

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
import com.komoui.themes.radius
import com.komoui.themes.styles

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
        // Caller modifier starts the chain; defaults follow so a caller can override size.
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
            disabledThumbColor = themeColors.mutedForeground.copy(alpha = 0.5f),
            disabledActiveTrackColor = themeColors.primary.copy(alpha = 0.5f),
            disabledInactiveTrackColor = themeColors.secondary.copy(alpha = 0.5f)
        ),
        thumb = {
            val borderColor = if (enabled) themeColors.primary else themeColors.primary.copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(themeColors.background)
                    .border(2.dp, borderColor, CircleShape)
            )
        },
        track = {
            val trackColor = if (enabled) themeColors.secondary else themeColors.secondary.copy(alpha = 0.5f)
            val activeTrackColor = if (enabled) themeColors.primary else themeColors.primary.copy(alpha = 0.5f)
            // Fraction of the active track; guard the empty range and clamp out-of-range values,
            // since fillMaxWidth() requires a 0..1 fraction.
            val span = valueRange.endInclusive - valueRange.start
            val activeTrackWidthFraction =
                if (span == 0f) 0f else ((value - valueRange.start) / span).coerceIn(0f, 1f)
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
