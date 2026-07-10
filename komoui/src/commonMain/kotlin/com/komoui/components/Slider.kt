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
import androidx.compose.material3.Slider as ComposeSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.komoui.themes.KomoStyles
import com.komoui.themes.radius
import com.komoui.themes.styles

/**
 * @param colors The [SliderStyle] resolving the slider's colors. See [SliderDefaults.colors].
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
    colors: SliderStyle = SliderDefaults.colors()
) {
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
        thumb = {
            val borderColor = if (enabled) colors.thumbBorder else colors.thumbBorder.copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(colors.thumb)
                    .border(2.dp, borderColor, CircleShape)
            )
        },
        track = {
            val trackColor = if (enabled) colors.track else colors.track.copy(alpha = 0.5f)
            val activeTrackColor = if (enabled) colors.activeTrack else colors.activeTrack.copy(alpha = 0.5f)
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

/** Colors used by a [Slider]. Disabled states are derived at 50% alpha. */
data class SliderStyle(
    val thumb: Color,
    val thumbBorder: Color,
    val track: Color,
    val activeTrack: Color,
)

object SliderDefaults {
    private fun colorsFrom(styles: KomoStyles): SliderStyle = SliderStyle(
        thumb = styles.background,
        thumbBorder = styles.primary,
        track = styles.secondary,
        activeTrack = styles.primary,
    )

    /** [SliderStyle] with the default KomoUI color scheme. */
    @Composable
    fun colors(): SliderStyle = colorsFrom(MaterialTheme.styles)

    /** [SliderStyle] with the default scheme, mutated by [overrides]. */
    @Composable
    fun colors(overrides: SliderStyle.() -> SliderStyle): SliderStyle =
        colorsFrom(MaterialTheme.styles).overrides()
}
