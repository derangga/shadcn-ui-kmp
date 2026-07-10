package com.komoui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * A single bar-chart series.
 *
 * @param key Stable identifier; used for animation keying and tooltip lookup.
 * @param label Human-readable label shown in legend / tooltip.
 * @param color Bar color.
 * @param points Ordered list of (x-category, y-value) points.
 */
@Immutable
data class BarSeries(
    val key: String,
    val label: String,
    val color: Color,
    val points: List<BarPoint>,
)

@Immutable
data class BarPoint(val x: String, val y: Float)

/**
 * A single line-chart series.
 *
 * @param key Stable identifier.
 * @param label Human-readable label for legend / tooltip.
 * @param color Stroke color.
 * @param points Ordered list of (x-category, y-value) points.
 */
@Immutable
data class LineSeries(
    val key: String,
    val label: String,
    val color: Color,
    val points: List<LinePoint>,
)

@Immutable
data class LinePoint(val x: String, val y: Float)

/**
 * A single area-chart series.
 *
 * @param key Stable identifier.
 * @param label Human-readable label for legend / tooltip.
 * @param color Stroke + fill base color.
 * @param points Ordered list of (x-category, y-value) points.
 */
@Immutable
data class AreaSeries(
    val key: String,
    val label: String,
    val color: Color,
    val points: List<AreaPoint>,
)

@Immutable
data class AreaPoint(val x: String, val y: Float)

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
 * Active scrub state for tap-and-drag tooltips. `index = null` means not scrubbing.
 */
@Stable
internal class ChartScrubState {
    var index: Int? by mutableStateOf(null)
    var pointer: Offset? by mutableStateOf(null)
}

@Composable
internal fun rememberChartScrubState(): ChartScrubState = remember { ChartScrubState() }

/**
 * Modifier that attaches tap-and-drag horizontal scrubbing. On press-down the
 * nearest column is selected; the selection follows the finger; release clears.
 *
 * This is **claim-on-down**: the first pointer-down is consumed immediately so
 * the tooltip appears under the finger without a touch-slop delay. As a
 * consequence it is **incompatible with `Modifier.horizontalScroll`**, which
 * uses a claim-after-touch-slop strategy — once this modifier consumes the
 * down event the scrollable parent never gets a chance to start scrolling.
 * Chart composables resolve this by disabling tooltips when `scrollable = true`.
 * See `Chart.md` in this directory (Pitfall #4) for alternatives if you need both.
 */
internal fun Modifier.chartScrub(
    columnCount: Int,
    plotLeft: Float,
    plotWidth: Float,
    onChange: (Int?, Offset?) -> Unit,
): Modifier = this.pointerInput(columnCount, plotLeft, plotWidth) {
    if (columnCount <= 0 || plotWidth <= 0f) return@pointerInput
    awaitPointerEventScope {
        while (true) {
            val down = awaitPointerEvent()
            val firstDown = down.changes.firstOrNull { it.pressed && !it.previousPressed } ?: continue
            firstDown.consume()
            emitScrub(firstDown.position, columnCount, plotLeft, plotWidth, onChange)
            var pointerId = firstDown.id
            while (true) {
                val event = awaitPointerEvent()
                val change = event.changes.firstOrNull { it.id == pointerId }
                if (change == null) {
                    onChange(null, null)
                    break
                }
                if (change.changedToUp()) {
                    onChange(null, null)
                    change.consume()
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
    onChange: (Int?, Offset?) -> Unit,
) {
    val rel = ((pos.x - plotLeft) / plotWidth).coerceIn(0f, 1f)
    val idx = (rel * columnCount).toInt().coerceIn(0, columnCount - 1)
    onChange(idx, pos)
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
