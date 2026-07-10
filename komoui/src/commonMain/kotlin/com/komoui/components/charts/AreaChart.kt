package com.komoui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.komoui.themes.styles
import kotlin.math.max

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
 * stay pinned on the left. Drag-scrub tooltips are force-disabled in this
 * mode: `chartScrub` claims the gesture on pointer-down while
 * `Modifier.horizontalScroll` claims after touch-slop, so they can't coexist
 * without changing one strategy (see `Chart.md` in this directory, Pitfall #4,
 * for the long-press / tap-only alternatives).
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
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = chartLabelStyle()
    val gridColor = MaterialTheme.styles.border
    val labelColor = MaterialTheme.styles.mutedForeground
    val indicatorColor = MaterialTheme.styles.mutedForeground
    val dotInnerColor = MaterialTheme.styles.popover

    val xLabels = series.firstOrNull()?.points?.map { it.x } ?: emptyList()
    val columnCount = xLabels.size

    val allValues = series.flatMap { s -> s.points.map { it.y } }
    val minValue = allValues.minOrNull() ?: 0f
    val maxValue = allValues.maxOrNull() ?: 1f
    val domain = computeDomain(minValue, maxValue, axisOptions.yDomain)
    val ticks = yTicks(domain, axisOptions.yTickCount)

    val density = LocalDensity.current
    val insets = AxisInsetsDp()
    val yAxisWidthDp = if (axisOptions.showY) insets.left else 0.dp
    val bottomInsetPx = with(density) { (if (axisOptions.showX) insets.bottom else 0.dp).toPx() }
    val strokePx = with(density) { strokeWidth.toPx() }
    val dotPx = with(density) { 3.dp.toPx() }

    val progress = remember { Animatable(if (animate) 0f else 1f) }
    LaunchedEffect(series) {
        if (animate) {
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis = 800))
        } else {
            progress.snapTo(1f)
        }
    }

    val scrub = rememberChartScrubState()
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val effectiveShowTooltip = showTooltip && !scrollable
    val plotAxisOptions = remember(axisOptions) { axisOptions.copy(showY = false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription =
                    chartSemanticsLabel(series.map { it.label }, series.firstOrNull()?.points?.size ?: 0)
            }
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(chartHeight)) {
            if (axisOptions.showY) {
                Canvas(modifier = Modifier.width(yAxisWidthDp).fillMaxHeight()) {
                    val yAxisRect = computePlotRect(size, PlotInsets(0f, bottomInsetPx))
                    drawYAxisOnly(
                        yAxisRect = yAxisRect,
                        yTicks = ticks,
                        domain = domain,
                        yLabelFormatter = axisOptions.yLabelFormatter,
                        textMeasurer = textMeasurer,
                        labelStyle = labelStyle,
                        labelColor = labelColor,
                    )
                }
            }

            BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxHeight()) {
                val viewportDp = maxWidth
                val rawContentDp = if (columnCount > 0) minColumnWidth * columnCount else viewportDp
                val contentWidthDp =
                    if (scrollable) maxOf(rawContentDp, viewportDp) else viewportDp
                val needsScroll = scrollable && contentWidthDp > viewportDp

                val scrollMod = if (needsScroll) {
                    Modifier.horizontalScroll(rememberScrollState())
                } else Modifier

                Box(modifier = Modifier.fillMaxSize().then(scrollMod)) {
                    val gestureMod = if (effectiveShowTooltip && columnCount > 0) {
                        Modifier.chartScrub(
                            columnCount = columnCount,
                            plotLeft = 0f,
                            plotWidth = canvasSize.width.toFloat(),
                        ) { idx, pos ->
                            scrub.index = idx
                            scrub.pointer = pos
                        }
                    } else Modifier

                    Canvas(
                        modifier = Modifier
                            .width(contentWidthDp)
                            .fillMaxHeight()
                            .onSizeChanged { canvasSize = it }
                            .then(gestureMod),
                    ) {
                        val plotRect = computePlotRect(size, PlotInsets(0f, bottomInsetPx))

                        drawAxesAndGrid(
                            plotRect = plotRect,
                            xLabels = xLabels,
                            yTicks = ticks,
                            domain = domain,
                            axisOptions = plotAxisOptions,
                            textMeasurer = textMeasurer,
                            labelStyle = labelStyle,
                            gridColor = gridColor,
                            labelColor = labelColor,
                        )

                        if (columnCount == 0 || series.isEmpty()) return@Canvas

                        val span = domain.endInclusive - domain.start
                        if (span <= 0f) return@Canvas

                        val slotWidth = plotRect.width / columnCount
                        val baselineY = plotRect.bottom -
                            ((0f - domain.start) / span) * plotRect.height
                        val clipRight = plotRect.left + plotRect.width * progress.value

                        clipRect(
                            left = 0f,
                            top = 0f,
                            right = clipRight,
                            bottom = size.height,
                        ) {
                            series.forEach { s ->
                                val points = seriesPositions(
                                    s.points.map { it.y }, plotRect, domain, slotWidth,
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
                                drawPath(
                                    path = linePath,
                                    color = s.color,
                                    style = Stroke(width = strokePx),
                                )
                            }
                        }

                        scrub.index?.let { idx ->
                            drawScrubIndicator(plotRect, columnCount, idx, indicatorColor)
                            series.forEach { s ->
                                val p = s.points.getOrNull(idx) ?: return@forEach
                                val cx = plotRect.left + slotWidth * (idx + 0.5f)
                                val cy = plotRect.bottom -
                                    ((p.y - domain.start) / span) * plotRect.height
                                drawCircle(
                                    color = s.color,
                                    radius = dotPx + 1.5f,
                                    center = Offset(cx, cy),
                                )
                                drawCircle(
                                    color = dotInnerColor,
                                    radius = dotPx,
                                    center = Offset(cx, cy),
                                )
                            }
                        }
                    }

                    val activeIdx = scrub.index
                    if (effectiveShowTooltip &&
                        activeIdx != null && activeIdx in 0 until columnCount
                    ) {
                        val slotWidthPx = canvasSize.width.toFloat() / columnCount
                        val centerX = slotWidthPx * (activeIdx + 0.5f)
                        val xDp = with(density) { centerX.toDp() }
                        Box(modifier = Modifier.padding(start = xDp + 8.dp, top = 8.dp)) {
                            ChartTooltip(
                                title = xLabels.getOrNull(activeIdx).orEmpty(),
                                entries = series.mapNotNull { s ->
                                    val p = s.points.getOrNull(activeIdx)
                                        ?: return@mapNotNull null
                                    TooltipEntry(
                                        label = s.label,
                                        value = axisOptions.yLabelFormatter(p.y),
                                        color = s.color,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }

        if (showLegend) {
            ChartLegend(items = series.map { it.label to it.color })
        }
    }
}
