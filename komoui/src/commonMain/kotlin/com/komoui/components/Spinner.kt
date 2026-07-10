package com.komoui.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.komoui.themes.styles

enum class SpinnerVariant {
    Circular,
    Bounce,
    Moon,
    Pulse
}

/**
 * An indeterminate loading indicator with four visual variants.
 *
 * @param modifier Modifier applied to the spinner container.
 * @param variant Visual style: [SpinnerVariant.Circular] (default), [SpinnerVariant.Bounce],
 * [SpinnerVariant.Moon], or [SpinnerVariant.Pulse].
 * @param size Overall size of the spinner. Defaults to 40.dp.
 * @param color Primary color of the spinner. Defaults to `MaterialTheme.styles.primary`.
 * @param strokeWidth Stroke width for stroke-based variants ([SpinnerVariant.Circular] and
 * [SpinnerVariant.Moon]). Ignored by [SpinnerVariant.Bounce] and [SpinnerVariant.Pulse].
 */
@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    variant: SpinnerVariant = SpinnerVariant.Circular,
    size: Dp = 40.dp,
    color: Color = MaterialTheme.styles.primary,
    strokeWidth: Dp = 3.dp
) {
    // Indeterminate progress semantics so the spinner is perceivable to TalkBack/VoiceOver
    // instead of being a purely decorative animation.
    val semanticsModifier = modifier.progressSemantics()
    when (variant) {
        SpinnerVariant.Circular -> CircularSpinner(semanticsModifier, size, color, strokeWidth)
        SpinnerVariant.Bounce -> BounceSpinner(semanticsModifier, size, color)
        SpinnerVariant.Moon -> MoonSpinner(semanticsModifier, size, color, strokeWidth)
        SpinnerVariant.Pulse -> PulseSpinner(semanticsModifier, size, color)
    }
}

@Composable
private fun CircularSpinner(
    modifier: Modifier,
    size: Dp,
    color: Color,
    strokeWidth: Dp
) {
    val transition = rememberInfiniteTransition(label = "circularSpinner")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "circularAngle"
    )

    Canvas(modifier = modifier.size(size)) {
        val stroke = strokeWidth.toPx()
        rotate(degrees = angle) {
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun BounceSpinner(
    modifier: Modifier,
    size: Dp,
    color: Color
) {
    val durationMillis = 2000
    val transition = rememberInfiniteTransition(label = "bounceSpinner")

    val scale1 by transition.animateFloatPhased(
        initialValue = 0f,
        targetValue = 1f,
        durationMillis = durationMillis / 2,
        offsetMillis = 0,
        easing = EaseInOut,
        label = "bounceScale1"
    )
    val alpha1 by transition.animateFloatPhased(
        initialValue = 1f,
        targetValue = 0.5f,
        durationMillis = durationMillis / 2,
        offsetMillis = 0,
        easing = EaseInOut,
        label = "bounceAlpha1"
    )
    val scale2 by transition.animateFloatPhased(
        initialValue = 0f,
        targetValue = 1f,
        durationMillis = durationMillis / 2,
        offsetMillis = durationMillis / 2,
        easing = EaseInOut,
        label = "bounceScale2"
    )
    val alpha2 by transition.animateFloatPhased(
        initialValue = 1f,
        targetValue = 0.5f,
        durationMillis = durationMillis / 2,
        offsetMillis = durationMillis / 2,
        easing = EaseInOut,
        label = "bounceAlpha2"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * scale1)
                .background(color = color.copy(alpha = alpha1), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(size * scale2)
                .background(color = color.copy(alpha = alpha2), shape = CircleShape)
        )
    }
}

@Composable
private fun MoonSpinner(
    modifier: Modifier,
    size: Dp,
    color: Color,
    strokeWidth: Dp
) {
    val transition = rememberInfiniteTransition(label = "moonSpinner")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = EaseInOut),
            repeatMode = RepeatMode.Restart
        ),
        label = "moonAngle"
    )

    Canvas(modifier = modifier.size(size)) {
        val stroke = strokeWidth.toPx()
        drawArc(
            color = color.copy(alpha = 0.3f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        rotate(degrees = angle) {
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 30f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun PulseSpinner(
    modifier: Modifier,
    size: Dp,
    color: Color
) {
    val durationMillis = 1400
    val delayBetweenDotsMillis = 160
    val transition = rememberInfiniteTransition(label = "pulseSpinner")

    val scale1 by transition.animateFloatPhased(
        initialValue = 0f,
        targetValue = 1f,
        durationMillis = durationMillis / 2,
        offsetMillis = 0,
        easing = EaseInOut,
        label = "pulseScale1"
    )
    val scale2 by transition.animateFloatPhased(
        initialValue = 0f,
        targetValue = 1f,
        durationMillis = durationMillis / 2,
        offsetMillis = delayBetweenDotsMillis,
        easing = EaseInOut,
        label = "pulseScale2"
    )
    val scale3 by transition.animateFloatPhased(
        initialValue = 0f,
        targetValue = 1f,
        durationMillis = durationMillis / 2,
        offsetMillis = delayBetweenDotsMillis * 2,
        easing = EaseInOut,
        label = "pulseScale3"
    )

    val dotSize = size * 3 / 11
    val gap = size / 11

    Row(
        modifier = modifier.size(size),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PulseDot(boxSize = dotSize, scale = scale1, color = color)
        Spacer(modifier = Modifier.width(gap))
        PulseDot(boxSize = dotSize, scale = scale2, color = color)
        Spacer(modifier = Modifier.width(gap))
        PulseDot(boxSize = dotSize, scale = scale3, color = color)
    }
}

@Composable
private fun PulseDot(boxSize: Dp, scale: Float, color: Color) {
    Box(modifier = Modifier.size(boxSize), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier.size(boxSize * scale),
            shape = CircleShape,
            color = color
        ) {}
    }
}

@Composable
private fun InfiniteTransition.animateFloatPhased(
    initialValue: Float,
    targetValue: Float,
    durationMillis: Int,
    offsetMillis: Int,
    easing: androidx.compose.animation.core.Easing,
    label: String
) = animateFloat(
    initialValue = initialValue,
    targetValue = targetValue,
    animationSpec = infiniteRepeatable(
        animation = tween(durationMillis = durationMillis, easing = easing),
        repeatMode = RepeatMode.Reverse,
        initialStartOffset = StartOffset(offsetMillis)
    ),
    label = label
)
