package com.komoui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.themes.radius
import com.komoui.themes.styles
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * A single (x-category, y-value) data point shared by all chart types.
 */
@Immutable
data class ChartPoint(val x: String, val y: Float)

/**
 * A single chart series shared by [BarChart], [LineChart] and [AreaChart].
 *
 * @param key Stable identifier; used for animation keying and tooltip lookup.
 * @param label Human-readable label shown in legend / tooltip.
 * @param color Series color (bar fill / line stroke / area stroke + fill base).
 * @param points Ordered list of (x-category, y-value) points.
 */
@Immutable
data class ChartSeries(
    val key: String,
    val label: String,
    val color: Color,
    val points: List<ChartPoint>,
)

// Chart-type-specific aliases kept for source compatibility; all charts share one model.
typealias BarPoint = ChartPoint
typealias BarSeries = ChartSeries
typealias LinePoint = ChartPoint
typealias LineSeries = ChartSeries
typealias AreaPoint = ChartPoint
typealias AreaSeries = ChartSeries

/**
 * Configuration for chart axes and gridlines.
 *
 * @param showX Show x-axis tick labels.
 * @param showY Show y-axis tick labels.
 * @param showHorizontalGrid Draw dashed gridlines at y-ticks.
 * @param showVerticalGrid Draw dashed gridlines at x-categories.
 * @param yTickCount Approximate number of y-ticks (including 0 and max).
 * @param yDomain Optional override for y-axis range. If null, computed from data.
 * @param xLabelFormatter Transforms raw x labels before rendering.
 * @param yLabelFormatter Transforms numeric y values into tick labels.
 */
@Immutable
data class ChartAxisOptions(
    val showX: Boolean = true,
    val showY: Boolean = true,
    val showHorizontalGrid: Boolean = true,
    val showVerticalGrid: Boolean = false,
    val yTickCount: Int = 4,
    val yDomain: ClosedFloatingPointRange<Float>? = null,
    val xLabelFormatter: (String) -> String = { it },
    val yLabelFormatter: (Float) -> String = ::defaultYFormatter,
)

internal fun defaultYFormatter(v: Float): String {
    val abs = abs(v)
    return when {
        abs >= 1_000_000f -> formatCompact(v / 1_000_000f) + "M"
        abs >= 1_000f -> formatCompact(v / 1_000f) + "K"
        v == v.toInt().toFloat() -> v.toInt().toString()
        else -> formatCompact(v)
    }
}

private fun formatCompact(v: Float): String {
    val rounded = (v * 10f).toInt() / 10f
    return if (rounded == rounded.toInt().toFloat()) rounded.toInt().toString()
    else rounded.toString()
}

/**
 * Rounds [value] up to a "nice" number of the form 1/2/5 × 10^n.
 * Used to compute a pleasant max for the y-axis.
 */
internal fun niceCeil(value: Float): Float {
    if (value <= 0f) return 1f
    val exp = floor(log10(value.toDouble())).toFloat()
    val pow10 = 10f.pow(exp)
    val frac = value / pow10
    val niceFrac = when {
        frac <= 1f -> 1f
        frac <= 2f -> 2f
        frac <= 5f -> 5f
        else -> 10f
    }
    return niceFrac * pow10
}

internal fun computeDomain(
    minValue: Float,
    maxValue: Float,
    override: ClosedFloatingPointRange<Float>?,
): ClosedFloatingPointRange<Float> {
    if (override != null) return override
    val rawMin = min(0f, minValue)
    val rawMax = if (maxValue <= 0f) 1f else niceCeil(maxValue)
    val lo = if (rawMin < 0f) -niceCeil(-rawMin) else 0f
    return lo..rawMax
}

internal fun yTicks(
    domain: ClosedFloatingPointRange<Float>,
    count: Int,
): List<Float> {
    val span = domain.endInclusive - domain.start
    if (span <= 0f || count <= 1) return listOf(domain.start, domain.endInclusive)
    val step = span / count
    return (0..count).map { domain.start + step * it }
}

/**
 * Holds the inset of the plot area within the canvas — i.e. how much space
 * the axis labels eat at the left and bottom.
 */
@Stable
internal data class PlotInsets(val left: Float, val bottom: Float)

internal fun computePlotRect(canvasSize: Size, insets: PlotInsets): Rect =
    Rect(
        left = insets.left,
        top = 0f,
        right = canvasSize.width,
        bottom = canvasSize.height - insets.bottom,
    )

/**
 * Active scrub state for drag tooltips. `index = null` means not scrubbing.
 */
@Stable
internal class ChartScrubState {
    var index: Int? by mutableStateOf(null)
}

@Composable
internal fun rememberChartScrubState(): ChartScrubState = remember { ChartScrubState() }

/**
 * Modifier that attaches horizontal drag scrubbing: the nearest column is selected once the drag
 * crosses the horizontal touch slop, the selection follows the finger, and release clears it.
 *
 * The gesture is claimed only after a **horizontal** drag is detected, so a vertical-scrolling
 * parent (a chart embedded in a scrollable page — the common case) still receives vertical drags.
 * It remains incompatible with `Modifier.horizontalScroll` (both want the horizontal slop), which
 * is why chart composables disable tooltips in `scrollable` mode.
 */
internal fun Modifier.chartScrub(
    columnCount: Int,
    plotLeft: Float,
    plotWidth: Float,
    onChange: (Int?) -> Unit,
): Modifier = this.pointerInput(columnCount, plotLeft, plotWidth) {
    if (columnCount <= 0 || plotWidth <= 0f) return@pointerInput
    awaitPointerEventScope {
        while (true) {
            val down = awaitFirstDown(requireUnconsumed = false)
            val dragStart = awaitHorizontalTouchSlopOrCancellation(down.id) { change, _ ->
                emitScrub(change.position, columnCount, plotLeft, plotWidth, onChange)
                change.consume()
            } ?: continue // cancelled or a vertical drag the parent should handle
            emitScrub(dragStart.position, columnCount, plotLeft, plotWidth, onChange)
            while (true) {
                val event = awaitPointerEvent()
                val change = event.changes.firstOrNull { it.id == down.id }
                if (change == null || change.changedToUp()) {
                    onChange(null)
                    change?.consume()
                    break
                }
                if (change.positionChanged()) {
                    emitScrub(change.position, columnCount, plotLeft, plotWidth, onChange)
                    change.consume()
                }
            }
        }
    }
}

private fun emitScrub(
    pos: Offset,
    columnCount: Int,
    plotLeft: Float,
    plotWidth: Float,
    onChange: (Int?) -> Unit,
) {
    val rel = ((pos.x - plotLeft) / plotWidth).coerceIn(0f, 1f)
    val idx = (rel * columnCount).toInt().coerceIn(0, columnCount - 1)
    onChange(idx)
}

/**
 * Draws horizontal grid lines, optional vertical grid lines, and tick labels.
 *
 * The caller is responsible for first computing the plot rectangle (i.e. leaving
 * space at the left for y labels and at the bottom for x labels).
 */
internal fun DrawScope.drawAxesAndGrid(
    plotRect: Rect,
    xLabels: List<String>,
    yTicks: List<Float>,
    domain: ClosedFloatingPointRange<Float>,
    axisOptions: ChartAxisOptions,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle,
    gridColor: Color,
    labelColor: Color,
) {
    val span = domain.endInclusive - domain.start
    if (span <= 0f) return

    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))

    if (axisOptions.showHorizontalGrid) {
        yTicks.forEach { tick ->
            val y = plotRect.bottom - ((tick - domain.start) / span) * plotRect.height
            drawLine(
                color = gridColor,
                start = Offset(plotRect.left, y),
                end = Offset(plotRect.right, y),
                strokeWidth = 1f,
                pathEffect = if (tick == domain.start) null else dashEffect,
            )
        }
    }

    if (axisOptions.showVerticalGrid && xLabels.isNotEmpty()) {
        val slot = plotRect.width / xLabels.size
        for (i in xLabels.indices) {
            val x = plotRect.left + slot * (i + 0.5f)
            drawLine(
                color = gridColor,
                start = Offset(x, plotRect.top),
                end = Offset(x, plotRect.bottom),
                strokeWidth = 1f,
                pathEffect = dashEffect,
            )
        }
    }

    if (axisOptions.showY) {
        yTicks.forEach { tick ->
            val y = plotRect.bottom - ((tick - domain.start) / span) * plotRect.height
            val label = axisOptions.yLabelFormatter(tick)
            val measured = textMeasurer.measure(label, labelStyle)
            drawText(
                textLayoutResult = measured,
                color = labelColor,
                topLeft = Offset(
                    x = plotRect.left - measured.size.width - 4f,
                    y = y - measured.size.height / 2f,
                ),
            )
        }
    }

    if (axisOptions.showX && xLabels.isNotEmpty()) {
        val slot = plotRect.width / xLabels.size
        xLabels.forEachIndexed { i, raw ->
            val centerX = plotRect.left + slot * (i + 0.5f)
            val label = axisOptions.xLabelFormatter(raw)
            val measured = textMeasurer.measure(label, labelStyle)
            drawText(
                textLayoutResult = measured,
                color = labelColor,
                topLeft = Offset(
                    x = centerX - measured.size.width / 2f,
                    y = plotRect.bottom + 6f,
                ),
            )
        }
    }
}

/**
 * Draws ONLY the y-axis tick labels into a dedicated, pinned canvas to the left
 * of the plot. Used when [ChartScaffold] runs in scrollable mode so the y-axis
 * stays in place while the plot scrolls horizontally.
 *
 * @param yAxisRect The drawable area of the y-axis canvas, excluding the
 *   x-axis label strip at the bottom. Use [computePlotRect] with the y-axis
 *   canvas size and the same bottom inset used by the plot.
 */
internal fun DrawScope.drawYAxisOnly(
    yAxisRect: Rect,
    yTicks: List<Float>,
    domain: ClosedFloatingPointRange<Float>,
    yLabelFormatter: (Float) -> String,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle,
    labelColor: Color,
) {
    val span = domain.endInclusive - domain.start
    if (span <= 0f) return
    yTicks.forEach { tick ->
        val y = yAxisRect.bottom - ((tick - domain.start) / span) * yAxisRect.height
        val label = yLabelFormatter(tick)
        val measured = textMeasurer.measure(label, labelStyle)
        drawText(
            textLayoutResult = measured,
            color = labelColor,
            topLeft = Offset(
                x = yAxisRect.right - measured.size.width - 4f,
                y = y - measured.size.height / 2f,
            ),
        )
    }
}

/**
 * Draws a vertical indicator line at the scrub column.
 */
internal fun DrawScope.drawScrubIndicator(
    plotRect: Rect,
    columnCount: Int,
    activeIndex: Int,
    color: Color,
) {
    if (columnCount <= 0) return
    val slot = plotRect.width / columnCount
    val x = plotRect.left + slot * (activeIndex + 0.5f)
    drawLine(
        color = color,
        start = Offset(x, plotRect.top),
        end = Offset(x, plotRect.bottom),
        strokeWidth = 1f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f)),
    )
}

/**
 * Screen-reader summary for a chart (series labels + points-per-series). The chart itself is a
 * silent Canvas; apply this via `semantics { contentDescription = ... }` on the chart root.
 * See issue shadcn-ui-kmp-mjl.3.
 */
internal fun chartSemanticsLabel(seriesLabels: List<String>, pointCount: Int): String =
    buildString {
        append("Chart, ")
        append(seriesLabels.size)
        append(" series (")
        append(seriesLabels.joinToString(", "))
        append("), ")
        append(pointCount)
        append(" data points each")
    }

/**
 * Per-frame plot geometry handed to a chart's draw lambda by [ChartScaffold].
 *
 * @property progress Animation progress in `0f..1f` (reveal / grow-in).
 * @property scrubIndex Active scrub column, or null when not scrubbing.
 */
@Immutable
internal class ChartPlotScope(
    val plotRect: Rect,
    val domain: ClosedFloatingPointRange<Float>,
    val span: Float,
    val columnCount: Int,
    val slotWidth: Float,
    val progress: Float,
    val scrubIndex: Int?,
)

/**
 * Shared chart scaffold behind [BarChart], [LineChart] and [AreaChart]. Owns the pinned y-axis,
 * horizontal-scroll wiring, drag-scrub gesture, axes/grid, scrub indicator, tooltip and legend;
 * the caller supplies only [drawPlot], which draws the bars / line / area for one frame.
 *
 * Domain and tick computation is remembered on the data (not on the whole [axisOptions], whose
 * formatter lambdas defeat data-class equality), so scrubbing doesn't recompute them every frame.
 */
@Composable
internal fun ChartScaffold(
    series: List<ChartSeries>,
    modifier: Modifier,
    axisOptions: ChartAxisOptions,
    showTooltip: Boolean,
    showLegend: Boolean,
    chartHeight: Dp,
    scrollable: Boolean,
    minColumnWidth: Dp,
    animate: Boolean,
    animationDurationMillis: Int,
    drawPlot: DrawScope.(ChartPlotScope) -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = chartLabelStyle()
    val gridColor = MaterialTheme.styles.border
    val labelColor = MaterialTheme.styles.mutedForeground
    val indicatorColor = MaterialTheme.styles.mutedForeground

    val xLabels = remember(series) { series.firstOrNull()?.points?.map { it.x } ?: emptyList() }
    val columnCount = xLabels.size

    val domain = remember(series, axisOptions.yDomain) {
        val allValues = series.flatMap { s -> s.points.map { it.y } }
        computeDomain(allValues.minOrNull() ?: 0f, allValues.maxOrNull() ?: 1f, axisOptions.yDomain)
    }
    val ticks = remember(domain, axisOptions.yTickCount) { yTicks(domain, axisOptions.yTickCount) }

    val density = LocalDensity.current
    val insets = AxisInsetsDp()
    val yAxisWidthDp = if (axisOptions.showY) insets.left else 0.dp
    val bottomInsetPx = with(density) { (if (axisOptions.showX) insets.bottom else 0.dp).toPx() }

    val progress = remember { Animatable(if (animate) 0f else 1f) }
    LaunchedEffect(series, animate) {
        if (animate) {
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis = animationDurationMillis))
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
                val contentWidthDp = if (scrollable) maxOf(rawContentDp, viewportDp) else viewportDp
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
                        ) { idx -> scrub.index = idx }
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

                        drawPlot(
                            ChartPlotScope(
                                plotRect = plotRect,
                                domain = domain,
                                span = span,
                                columnCount = columnCount,
                                slotWidth = slotWidth,
                                progress = progress.value,
                                scrubIndex = scrub.index,
                            )
                        )

                        scrub.index?.let { idx ->
                            drawScrubIndicator(plotRect, columnCount, idx, indicatorColor)
                        }
                    }

                    val activeIdx = scrub.index
                    if (effectiveShowTooltip && activeIdx != null && activeIdx in 0 until columnCount) {
                        val slotWidthPx = canvasSize.width.toFloat() / columnCount
                        val centerX = slotWidthPx * (activeIdx + 0.5f)
                        val title = xLabels.getOrNull(activeIdx).orEmpty()
                        val entries = series.mapNotNull { s ->
                            val p = s.points.getOrNull(activeIdx) ?: return@mapNotNull null
                            TooltipEntry(s.label, axisOptions.yLabelFormatter(p.y), s.color)
                        }
                        // Flip the tooltip to the left of the scrub line for right-half columns so
                        // it doesn't clip off the right edge.
                        if (centerX > canvasSize.width / 2f) {
                            val endDp = with(density) { (canvasSize.width - centerX).toDp() }
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                contentAlignment = Alignment.TopEnd,
                            ) {
                                Box(modifier = Modifier.padding(end = endDp + 8.dp)) {
                                    ChartTooltip(title = title, entries = entries)
                                }
                            }
                        } else {
                            val xDp = with(density) { centerX.toDp() }
                            Box(modifier = Modifier.padding(start = xDp + 8.dp, top = 8.dp)) {
                                ChartTooltip(title = title, entries = entries)
                            }
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

/**
 * Small flow-row of color-dot + label chips, rendered below the chart when enabled.
 */
@Composable
internal fun ChartLegend(items: List<Pair<String, Color>>, modifier: Modifier = Modifier) {
    if (items.isEmpty()) return
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 12.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
            16.dp,
            Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { (label, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = color, shape = CircleShape),
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.styles.mutedForeground,
                )
            }
        }
    }
}

/**
 * Tooltip card showing values for the active scrub column. Caller positions it.
 */
@Composable
internal fun ChartTooltip(
    title: String,
    entries: List<TooltipEntry>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.styles.popover,
                shape = RoundedCornerShape(MaterialTheme.radius.md),
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.styles.border,
                shape = RoundedCornerShape(MaterialTheme.radius.md),
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.styles.popoverForeground,
            )
            entries.forEach { entry ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color = entry.color, shape = CircleShape),
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = entry.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.styles.mutedForeground,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = entry.value,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.styles.popoverForeground,
                    )
                }
            }
        }
    }
}

@Immutable
internal data class TooltipEntry(val label: String, val value: String, val color: Color)

/** Standard label style for axis ticks. Kept centralised for consistency. */
@Composable
internal fun chartLabelStyle(): TextStyle = MaterialTheme.typography.labelSmall.copy(
    color = MaterialTheme.styles.mutedForeground,
    fontSize = 11.sp,
    textAlign = TextAlign.Center,
)

/** Default insets reserved for axis labels (in pixels). */
internal data class AxisInsetsDp(val left: Dp = 36.dp, val bottom: Dp = 22.dp)
