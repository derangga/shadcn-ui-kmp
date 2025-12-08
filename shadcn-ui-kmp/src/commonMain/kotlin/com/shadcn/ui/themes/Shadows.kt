package com.shadcn.ui.themes

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BoxShadow(
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp,
    val blurRadius: Dp = 0.dp,
    val spread: Dp = 0f.dp,
    val color: Color = Color.Gray,
)

fun Modifier.drawShadow(radius: Dp, shadow: BoxShadow): Modifier {
    return this.drawBehind {
        drawShadowLayer(
            radius = radius,
            shadow = shadow
        )
    }
}

fun Modifier.drawShadows(radius: Dp, shadows: List<BoxShadow>): Modifier {
    return this.drawBehind {
        shadows.forEach { shadow ->
            drawShadowLayer(
                radius = radius * 2,
                shadow = shadow
            )
        }
    }
}

private fun DrawScope.drawShadowLayer(
    radius: Dp,
    shadow: BoxShadow
) {
    val spreadPx = shadow.spread.toPx()
    val blurRadiusPx = shadow.blurRadius.toPx()

    // Calculate the bounds with spread
    val left = -spreadPx + shadow.offsetX.toPx()
    val top = -spreadPx + shadow.offsetY.toPx()
    val right = size.width + spreadPx + shadow.offsetX.toPx()
    val bottom = size.height + spreadPx + shadow.offsetY.toPx()

    val radiusPx = radius.toPx()

    if (blurRadiusPx > 0) {
        // Draw multiple layers to simulate blur effect
        val layers = (blurRadiusPx / 2).toInt().coerceAtLeast(1)

        for (i in 0 until layers) {
            val progress = i.toFloat() / layers
            val alpha = (1f - progress) * 0.3f // Gradually fade out
            val expansion = progress * blurRadiusPx

            drawRoundRect(
                color = shadow.color.copy(alpha = shadow.color.alpha * alpha),
                topLeft = Offset(
                    left - expansion,
                    top - expansion
                ),
                size = androidx.compose.ui.geometry.Size(
                    right - left + expansion * 2,
                    bottom - top + expansion * 2
                ),
                cornerRadius = CornerRadius(radiusPx + expansion)
            )
        }
    } else {
        // No blur, just draw a single layer
        drawRoundRect(
            color = shadow.color,
            topLeft = Offset(left, top),
            size = androidx.compose.ui.geometry.Size(
                right - left,
                bottom - top
            ),
            cornerRadius = CornerRadius(radiusPx)
        )
    }
}