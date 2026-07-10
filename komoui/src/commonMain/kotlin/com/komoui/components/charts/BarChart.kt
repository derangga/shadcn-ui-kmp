package com.komoui.components.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
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
 * When [showTooltip] is true (and [scrollable] is false), drag horizontally to
 * scrub through categories — a tooltip appears with all series values for the
 * active column.
 *
 * When [scrollable] is true the plot area scrolls horizontally and each
 * x-category is given at least [minColumnWidth] of space. The y-axis labels
 * stay pinned on the left. Drag-scrub tooltips are force-disabled in this mode.
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
    val density = LocalDensity.current
    val cornerPx = with(density) { barCornerRadius.toPx() }
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = chartLabelStyle()
    val labelColor = MaterialTheme.styles.mutedForeground

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
        animationDurationMillis = 600,
    ) { scope ->
        val plotRect = scope.plotRect
        val span = scope.span
        val growth = scope.progress
        val baselineY = plotRect.bottom - ((0f - scope.domain.start) / span) * plotRect.height
        val groupWidth = scope.slotWidth * barWidthFraction
        val barWidth = groupWidth / series.size

        for (col in 0 until scope.columnCount) {
            val slotCenter = plotRect.left + scope.slotWidth * (col + 0.5f)
            val groupLeft = slotCenter - groupWidth / 2f
            series.forEachIndexed { si, s ->
                val point = s.points.getOrNull(col) ?: return@forEachIndexed
                val targetY = plotRect.bottom - ((point.y - scope.domain.start) / span) * plotRect.height
                val animatedTop = baselineY + (targetY - baselineY) * growth
                val barLeft = groupLeft + si * barWidth
                val top = min(animatedTop, baselineY)
                val bottom = max(animatedTop, baselineY)
                drawRoundRect(
                    color = s.color,
                    topLeft = Offset(barLeft + barWidth * 0.08f, top),
                    size = Size(width = barWidth * 0.84f, height = max(0f, bottom - top)),
                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                )

                if (showValueLabels && growth >= 1f) {
                    val labelText = axisOptions.yLabelFormatter(point.y)
                    val measured = textMeasurer.measure(labelText, labelStyle)
                    drawText(
                        textLayoutResult = measured,
                        color = labelColor,
                        topLeft = Offset(
                            x = barLeft + barWidth / 2f - measured.size.width / 2f,
                            y = top - measured.size.height - 4f,
                        ),
                    )
                }
            }
        }
    }
}
