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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.komoui.themes.styles
import kotlin.math.max
import kotlin.math.min

/**
 * Vertical bar chart supporting one or more grouped series.
 *
 * Each series in [series] is expected to share the same x-categories with the
 * first series; categories beyond the first series' point list are ignored.
 * When [showValueLabels] is true the y-value is drawn just above each bar.
 * When [showTooltip] is true (and [scrollable] is false), press-and-drag
 * horizontally to scrub through categories — a tooltip appears with all
 * series values for the active column.
 *
 * When [scrollable] is true the plot area scrolls horizontally and each
 * x-category is given at least [minColumnWidth] of space. The y-axis labels
 * stay pinned on the left. Drag-scrub tooltips are force-disabled in this
 * mode: `chartScrub` claims the gesture on pointer-down while
 * `Modifier.horizontalScroll` claims after touch-slop, so they can't coexist
 * without changing one strategy (see `Chart.md` in this directory, Pitfall #4,
 * for the long-press / tap-only alternatives).
 *
 * Bars animate from height 0 to their final height on first composition when
 * [animate] is true.
 */
@Composable
fun BarChart(
    series: List<BarSeries>,
    modifier: Modifier = Modifier,
    axisOptions: ChartAxisOptions = ChartAxisOptions(),
    showTooltip: Boolean = true,
    showLegend: Boolean = false,
    showValueLabels: Boolean = false,
    barCornerRadius: Dp = 4.dp,
    barWidthFraction: Float = 0.7f,
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
    val cornerPx = with(density) { barCornerRadius.toPx() }

    val growth = remember { Animatable(if (animate) 0f else 1f) }
    LaunchedEffect(series) {
        if (animate) {
            growth.snapTo(0f)
            growth.animateTo(1f, tween(durationMillis = 600))
        } else {
            growth.snapTo(1f)
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
            // Pinned y-axis canvas
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

            // Plot area (optionally scrollable)
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

                        val baselineY = plotRect.bottom -
                            ((0f - domain.start) / span) * plotRect.height
                        val slotWidth = plotRect.width / columnCount
                        val groupWidth = slotWidth * barWidthFraction
                        val barWidth = groupWidth / series.size

                        for (col in 0 until columnCount) {
                            val slotCenter = plotRect.left + slotWidth * (col + 0.5f)
                            val groupLeft = slotCenter - groupWidth / 2f
                            series.forEachIndexed { si, s ->
                                val point = s.points.getOrNull(col) ?: return@forEachIndexed
                                val targetY = plotRect.bottom -
                                    ((point.y - domain.start) / span) * plotRect.height
                                val animatedTop = baselineY + (targetY - baselineY) * growth.value
                                val barLeft = groupLeft + si * barWidth
                                val top = min(animatedTop, baselineY)
                                val bottom = max(animatedTop, baselineY)
                                drawRoundRect(
                                    color = s.color,
                                    topLeft = Offset(barLeft + barWidth * 0.08f, top),
                                    size = Size(
                                        width = barWidth * 0.84f,
                                        height = max(0f, bottom - top),
                                    ),
                                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                                )

                                if (showValueLabels && growth.value >= 1f) {
                                    val labelText = axisOptions.yLabelFormatter(point.y)
                                    val measured = textMeasurer.measure(labelText, labelStyle)
                                    drawText(
                                        textLayoutResult = measured,
                                        color = labelColor,
                                        topLeft = Offset(
                                            x = barLeft + barWidth / 2f -
                                                measured.size.width / 2f,
                                            y = top - measured.size.height - 4f,
                                        ),
                                    )
                                }
                            }
                        }

                        scrub.index?.let { idx ->
                            drawScrubIndicator(plotRect, columnCount, idx, indicatorColor)
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
