package com.shadcn.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

/**
 * A Jetpack Compose Skeleton component inspired by Shadcn UI.
 * Displays a placeholder loading state with a shimmer effect.
 *
 * @param modifier The modifier to be applied to the skeleton container.
 * @param shape The shape of the skeleton. Defaults to `RoundedCornerShape(Radius.md)`.
 * @param baseColor The base background color of the skeleton. Defaults to `MaterialTheme.shadcnColors.muted`.
 * @param shimmerColor The color of the shimmering highlight. Defaults to `MaterialTheme.shadcnColors.background.copy(alpha = 0.8f)`.
 * @param animationDurationMillis The duration of one shimmer cycle in milliseconds.
 * @param gradientWidthRatio The ratio of the shimmer gradient's width to the skeleton's width.
 */
@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(MaterialTheme.radius.md),
    baseColor: Color = MaterialTheme.styles.muted,
    shimmerColor: Color = MaterialTheme.styles.muted.copy(alpha = 0.5f),
    animationDurationMillis: Int = 1500,
    gradientWidthRatio: Float = 0.5f
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val xShimmerTranslate by transition.animateFloat(
        initialValue = -size.width.toFloat() * (1f + gradientWidthRatio),
        targetValue = size.width.toFloat() * (1f + gradientWidthRatio),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "xShimmerTranslate"
    )

    val gradientColors = listOf(
        baseColor,
        shimmerColor,
        baseColor
    )

    val brush = Brush.linearGradient(
        colors = gradientColors,
        // The start and end X coordinates of the gradient, translated by xShimmerTranslate
        start = Offset(xShimmerTranslate, 0f),
        end = Offset(xShimmerTranslate + size.width * gradientWidthRatio, size.height.toFloat())
    )

    Box(
        modifier = modifier
            .onGloballyPositioned {
                size = it.size
            }
            .background(brush, shape)
    )
}
