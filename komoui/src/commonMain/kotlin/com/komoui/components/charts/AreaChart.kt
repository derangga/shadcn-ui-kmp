package com.komoui.components.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.komoui.themes.styles

/**
 * Area chart supporting one or more series. Each series renders as a stroked
 * line over a filled area down to the baseline.
 *
 * When [gradientFill] is true the fill is a vertical gradient from the series
 * color (alpha 0.4 at top) to transparent at the baseline. When false a flat
 * fill at [fillAlpha] is used.
 *
 * When [scrollable] is true the plot area scrolls horizontally and each
 * x-category is given at least [minColumnWidth] of space. The y-axis labels
 * stay pinned on the left. Drag-scrub tooltips are force-disabled in this mode.
 *
 * Lines reveal left to right on first composition when [animate] is true.
 */
@Composable
fun AreaChart(
    series: List<AreaSeries>,
    modifier: Modifier = Modifier,
    axisOptions: ChartAxisOptions = ChartAxisOptions(),
    showTooltip: Boolean = true,
    showLegend: Boolean = false,
    strokeWidth: Dp = 2.dp,
    smooth: Boolean = true,
    gradientFill: Boolean = true,
    fillAlpha: Float = 0.25f,
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
        val baselineY = plotRect.bottom - ((0f - scope.domain.start) / scope.span) * plotRect.height
        val clipRight = plotRect.left + plotRect.width * scope.progress

        clipRect(left = 0f, top = 0f, right = clipRight, bottom = size.height) {
            series.forEach { s ->
                // Clamp to the shared column count so a longer series doesn't draw past the plot.
                val points = seriesPositions(
                    s.points.take(scope.columnCount).map { it.y }, plotRect, scope.domain, slotWidth,
                )
                if (points.isEmpty()) return@forEach

                val linePath = buildLinePath(points, smooth)
                val areaPath = Path().apply {
                    addPath(linePath)
                    lineTo(points.last().x, baselineY)
                    lineTo(points.first().x, baselineY)
                    close()
                }

                val brush = if (gradientFill) {
                    Brush.verticalGradient(
                        colors = listOf(
                            s.color.copy(alpha = 0.40f),
                            s.color.copy(alpha = 0.0f),
                        ),
                        startY = plotRect.top,
                        endY = baselineY,
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            s.color.copy(alpha = fillAlpha),
                            s.color.copy(alpha = fillAlpha),
                        ),
                    )
                }

                drawPath(path = areaPath, brush = brush)
                drawPath(path = linePath, color = s.color, style = Stroke(width = strokePx))
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
