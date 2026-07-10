package com.komoui.components.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.komoui.themes.styles

/**
 * Line chart supporting one or more series.
 *
 * When [smooth] is true points are connected with cubic Bézier segments
 * (Catmull-Rom to Bézier conversion). When false, straight line segments
 * are drawn between points.
 *
 * When [scrollable] is true the plot area scrolls horizontally and each
 * x-category is given at least [minColumnWidth] of space. The y-axis labels
 * stay pinned on the left. Drag-scrub tooltips are force-disabled in this mode.
 *
 * Lines animate from left to right on first composition when [animate] is
 * true. When [showTooltip] is true (and [scrollable] is false), drag
 * horizontally to scrub.
 */
@Composable
fun LineChart(
    series: List<LineSeries>,
    modifier: Modifier = Modifier,
    axisOptions: ChartAxisOptions = ChartAxisOptions(),
    showTooltip: Boolean = true,
    showLegend: Boolean = false,
    strokeWidth: Dp = 2.dp,
    smooth: Boolean = true,
    animate: Boolean = true,
    chartHeight: Dp = 220.dp,
    scrollable: Boolean = false,
    minColumnWidth: Dp = 56.dp,
) {
    val density = LocalDensity.current
    val strokePx = with(density) { strokeWidth.toPx() }
    val dotPx = with(density) { 3.dp.toPx() }
    val dotInnerColor = MaterialTheme.styles.popover

    ChartScaffold(
        series = series,
        modifier = modifier,
        axisOptions = axisOptions,
        showTooltip = showTooltip,
        showLegend = showLegend,
        chartHeight = chartHeight,
        scrollable = scrollable,
        minColumnWidth = minColumnWidth,
        animate = animate,
        animationDurationMillis = 800,
    ) { scope ->
        val plotRect = scope.plotRect
        val slotWidth = scope.slotWidth
        val clipRight = plotRect.left + plotRect.width * scope.progress

        clipRect(left = 0f, top = 0f, right = clipRight, bottom = size.height) {
            series.forEach { s ->
                // Clamp to the shared column count so a longer series doesn't draw past the plot.
                val points = seriesPositions(
                    s.points.take(scope.columnCount).map { it.y }, plotRect, scope.domain, slotWidth,
                )
                val path = buildLinePath(points, smooth)
                drawPath(path = path, color = s.color, style = Stroke(width = strokePx))
            }
        }

        scope.scrubIndex?.let { idx ->
            series.forEach { s ->
                val p = s.points.getOrNull(idx) ?: return@forEach
                val cx = plotRect.left + slotWidth * (idx + 0.5f)
                val cy = plotRect.bottom - ((p.y - scope.domain.start) / scope.span) * plotRect.height
                drawCircle(color = s.color, radius = dotPx + 1.5f, center = Offset(cx, cy))
                drawCircle(color = dotInnerColor, radius = dotPx, center = Offset(cx, cy))
            }
        }
    }
}

internal fun seriesPositions(
    values: List<Float>,
    plotRect: Rect,
    domain: ClosedFloatingPointRange<Float>,
    slotWidth: Float,
): List<Offset> {
    val span = domain.endInclusive - domain.start
    return values.mapIndexed { i, v ->
        Offset(
            x = plotRect.left + slotWidth * (i + 0.5f),
            y = plotRect.bottom - ((v - domain.start) / span) * plotRect.height,
        )
    }
}

/**
 * Builds a Path through [points]. When [smooth] is true uses Catmull-Rom-to-Bézier
 * conversion to produce a smooth curve; otherwise uses straight segments.
 */
internal fun buildLinePath(points: List<Offset>, smooth: Boolean): Path {
    val path = Path()
    if (points.isEmpty()) return path
    path.moveTo(points[0].x, points[0].y)
    if (points.size == 1) return path

    if (!smooth) {
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }
        return path
    }

    for (i in 0 until points.size - 1) {
        val p0 = points[(i - 1).coerceAtLeast(0)]
        val p1 = points[i]
        val p2 = points[i + 1]
        val p3 = points[(i + 2).coerceAtMost(points.size - 1)]

        val c1x = p1.x + (p2.x - p0.x) / 6f
        val c1y = p1.y + (p2.y - p0.y) / 6f
        val c2x = p2.x - (p3.x - p1.x) / 6f
        val c2y = p2.y - (p3.y - p1.y) / 6f

        path.cubicTo(c1x, c1y, c2x, c2y, p2.x, p2.y)
    }
    return path
}
