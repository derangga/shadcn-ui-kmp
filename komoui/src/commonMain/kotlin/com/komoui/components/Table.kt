package com.komoui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
import com.komoui.themes.radius
import com.komoui.themes.komoStrings
import com.komoui.themes.styles
import com.komoui.utils.komoClickable

/**
 * Root Table primitive. Wraps content in a horizontally scrollable container with a rounded
 * border. Children are typically [TableHeader], [TableBody],
 * [TableFooter], and an optional [TableCaption] outside the scroll container.
 *
 * On mobile, the table is intentionally not shrunk to viewport — set explicit widths on cells
 * (typically via per-column [Dp] widths) and let users scroll horizontally.
 *
 * @param modifier The modifier to be applied to the outer container.
 * @param scrollState Horizontal scroll state shared by header and body.
 * @param content Table sections. Provide [TableHeader], [TableBody], and optionally [TableFooter].
 */
@Composable
fun Table(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable ColumnScope.() -> Unit
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(radius.md))
            .border(1.dp, styles.border, RoundedCornerShape(radius.md))
            .horizontalScroll(scrollState),
        content = content
    )
}

/**
 * Header section of a [Table]. Stack [TableRow]s vertically. Each header row renders a bottom
 * border to separate it from the body.
 */
@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier, content = content)
}

/**
 * Body section of a [Table]. Stack [TableRow]s vertically. The last row should pass
 * `showBottomBorder = false`.
 */
@Composable
fun TableBody(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier, content = content)
}

/**
 * Footer section of a [Table]. Renders a muted background and a top border, matching
 * `<TableFooter>`.
 */
@Composable
fun TableFooter(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val styles = MaterialTheme.styles
    Column(
        modifier = modifier
            .background(styles.muted)
            .border(1.dp, styles.border, RoundedCornerShape(0.dp)),
        content = content
    )
}

/**
 * A single row in a [Table]. Children are laid out horizontally inside a [RowScope]. The row
 * draws a bottom border (toggleable via [showBottomBorder]) and a selected/hover background.
 *
 * @param modifier The modifier to be applied to the row. Pass [Modifier.width] to make a row
 *      span the same total width as its siblings under [Table]'s horizontal scroll.
 * @param onClick Optional click handler. When non-null, the row is clickable.
 * @param selected When true, the row paints a muted background to indicate selection.
 * @param showBottomBorder When false, the row omits its bottom border — pass false for the
 *      final body row.
 * @param content Cells of the row — typically [TableHead] or [TableCell].
 */
@Composable
fun TableRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    selected: Boolean = false,
    showBottomBorder: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val styles = MaterialTheme.styles
    val background = if (selected) styles.muted else styles.card
    val clickModifier = if (onClick != null) {
        Modifier.komoClickable(
            onClick = onClick,
            role = Role.Button,
            stateDescription = if (selected) "Selected" else null,
        )
    } else Modifier
    Row(
        modifier = modifier
            .background(background)
            .then(clickModifier)
            .then(
                if (showBottomBorder) {
                    Modifier.bottomBorder(styles.border)
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/**
 * Header cell. 40.dp minimum height, 8.dp horizontal padding, medium-weight text in
 * [com.komoui.themes.KomoStyles.mutedForeground]. Wrap text in [Text] for the typical case.
 *
 * @param modifier The modifier to be applied to the cell. Pass [Modifier.width] to set the
 *      column's width — header and body cells should share the same width.
 * @param content Cell content, usually a [Text].
 */
@Composable
fun TableHead(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/**
 * Body cell. 8.dp padding, [com.komoui.themes.KomoStyles.foreground] text color.
 *
 * @param modifier The modifier to be applied to the cell. Pass [Modifier.width] to set the
 *      column's width — header and body cells should share the same width.
 * @param content Cell content.
 */
@Composable
fun TableCell(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/**
 * Caption rendered below a [Table] in muted small text. Place outside the [Table] container.
 *
 * @param text The caption text.
 * @param modifier The modifier to be applied to the caption.
 */
@Composable
fun TableCaption(text: String, modifier: Modifier = Modifier) {
    val styles = MaterialTheme.styles
    Text(
        text = text,
        color = styles.mutedForeground,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier.padding(top = 8.dp)
    )
}

/** Sort direction for a [DataTable] column. [NONE] means the column is unsorted. */
enum class SortDirection { NONE, ASC, DESC }

/**
 * Current sort state of a [DataTable].
 *
 * @param columnId The id of the column being sorted, or null when no column is sorted.
 * @param direction The active sort direction.
 */
data class SortState(
    val columnId: String? = null,
    val direction: SortDirection = SortDirection.NONE
)

/**
 * Column definition for [DataTable].
 *
 * @param id Stable identifier used as the sort key and visibility key.
 * @param header Header label text. Use [headerContent] for fully custom headers (the [header]
 *      string is still used as the accessible label and falls back to text when no custom
 *      content is provided).
 * @param width Fixed Dp width applied to header and body cells in this column.
 * @param sortable When true, clicking the header cycles NONE -> ASC -> DESC -> NONE. Requires
 *      [comparator].
 * @param comparator Comparator used when [sortable] is true.
 * @param headerContent Optional custom header cell content. When null the [header] string is
 *      rendered as a medium-weight muted label.
 * @param cell Renders one cell for the given row item.
 */
data class DataTableColumn<T>(
    val id: String,
    val header: String,
    val width: Dp,
    val sortable: Boolean = false,
    val comparator: Comparator<T>? = null,
    val headerContent: (@Composable RowScope.() -> Unit)? = null,
    val cell: @Composable RowScope.(T) -> Unit
)

/**
 * Scope receiver for the [DataTable] `pagination` slot. Exposes the current pagination
 * state and navigation actions so callers can render a custom pagination UI.
 */
interface PaginationScope {
    /** Current page index (0-based). */
    val page: Int

    /** Total number of pages. Always at least 1. */
    val pageCount: Int

    /** True when [prev] will advance the page backward. */
    val canPrev: Boolean

    /** True when [next] will advance the page forward. */
    val canNext: Boolean

    /** Go to the previous page if [canPrev]. */
    fun prev()

    /** Go to the next page if [canNext]. */
    fun next()

    /** Jump to an absolute 0-based page index (coerced into range). */
    fun goTo(page: Int)
}

/**
 * Default pagination control rendered when no `pagination` slot is supplied. Renders a
 * full-width row containing Previous/Next outline buttons aligned to the end. The "Page X of Y"
 * indicator is rendered separately by [DataTable] in the summary row above.
 */
@Composable
fun PaginationScope.DefaultPagination() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Button(
            onClick = ::prev,
            variant = ButtonVariant.Outline,
            size = ButtonSize.Sm,
            enabled = canPrev
        ) {
            Text(MaterialTheme.komoStrings.previous)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = ::next,
            variant = ButtonVariant.Outline,
            size = ButtonSize.Sm,
            enabled = canNext
        ) {
            Text(MaterialTheme.komoStrings.next)
        }
    }
}

/**
 * A typed Data Table. Provides sorting, optional row
 * selection, and Previous/Next pagination. State is owned by the component; pass [onSortChange]
 * and [onSelectionChange] to observe.
 *
 * The table scrolls horizontally — declare per-column [DataTableColumn.width] in [Dp] and the
 * row width is the sum of those plus the optional selection column.
 *
 * Note: the existing [Checkbox] does not support an indeterminate visual; the header checkbox
 * is checked iff all rows on the current page are selected, and clicking it toggles the whole
 * page.
 *
 * @param items Full unsorted, unpaginated data set.
 * @param columns Column definitions.
 * @param rowKey Stable key for an item. Used for selection identity.
 * @param modifier Modifier applied to the outer container.
 * @param enableSelection When true a leading checkbox column is auto-prepended.
 * @param selectionColumnWidth Width of the auto-injected selection column.
 * @param pageSize Rows per page.
 * @param initialSort Initial sort state.
 * @param onSortChange Observer callback fired when sort changes.
 * @param onSelectionChange Observer callback fired when selection changes. Emits the set of
 *      selected [rowKey] values, so selection survives item-instance refreshes and never
 *      contains duplicates for the same row.
 * @param onRowClick Optional row click handler.
 * @param caption Optional caption text rendered below the table.
 * The footer below the table is laid out as two rows: a summary row showing "N of M
 * row(s) selected." on the left and "Page X of Y" on the right, followed by the
 * [pagination] slot which spans the full width below.
 *
 * @param pagination Slot rendered as a full-width row below the summary line. Defaults to
 *      [DefaultPagination] (end-aligned Previous/Next buttons). The slot receives a
 *      [PaginationScope] exposing page state and navigation actions, so callers can fully
 *      replace the pagination UI (e.g. numbered pages, jump-to controls).
 */
@Composable
fun <T> DataTable(
    items: List<T>,
    columns: List<DataTableColumn<T>>,
    rowKey: (T) -> Any,
    modifier: Modifier = Modifier,
    enableSelection: Boolean = false,
    selectionColumnWidth: Dp = 48.dp,
    pageSize: Int = 10,
    initialSort: SortState = SortState(),
    onSortChange: ((SortState) -> Unit)? = null,
    onSelectionChange: ((Set<Any>) -> Unit)? = null,
    onRowClick: ((T) -> Unit)? = null,
    caption: String? = null,
    pagination: @Composable PaginationScope.() -> Unit = { DefaultPagination() }
) {
    val styles = MaterialTheme.styles

    var sort by remember { mutableStateOf(initialSort) }
    val updateSort: (SortState) -> Unit = { next ->
        sort = next
        onSortChange?.invoke(next)
    }

    // Selection is stored by rowKey, not item instance, so it survives item refreshes
    // and can never hold two entries for the same row.
    var selectedKeySet by remember { mutableStateOf<Set<Any>>(emptySet()) }
    val updateSelection: (Set<Any>) -> Unit = { next ->
        selectedKeySet = next
        onSelectionChange?.invoke(next)
    }

    val sorted = remember(items, sort, columns) {
        val col = columns.firstOrNull { it.id == sort.columnId }
        val cmp = col?.comparator
        when {
            cmp == null || sort.direction == SortDirection.NONE -> items
            sort.direction == SortDirection.ASC -> items.sortedWith(cmp)
            else -> items.sortedWith(cmp.reversed())
        }
    }

    val totalPages = if (sorted.isEmpty()) 1 else (sorted.size + pageSize - 1) / pageSize
    val pageState = remember { mutableStateOf(0) }
    val currentPage = pageState.value.coerceIn(0, totalPages - 1)
    LaunchedEffect(totalPages) {
        pageState.value = pageState.value.coerceIn(0, totalPages - 1)
    }
    val pageItems = sorted.drop(currentPage * pageSize).take(pageSize)
    val pageKeys = pageItems.map(rowKey).toSet()
    val allOnPageSelected = pageKeys.isNotEmpty() && selectedKeySet.containsAll(pageKeys)

    val paginationScope = remember(totalPages) {
        object : PaginationScope {
            override val page: Int get() = pageState.value
            override val pageCount: Int get() = totalPages
            override val canPrev: Boolean get() = pageState.value > 0
            override val canNext: Boolean get() = pageState.value < totalPages - 1
            override fun prev() {
                if (canPrev) pageState.value--
            }

            override fun next() {
                if (canNext) pageState.value++
            }

            override fun goTo(page: Int) {
                pageState.value = page.coerceIn(0, totalPages - 1)
            }
        }
    }

    val totalWidth = columns.fold(0.dp) { acc, c -> acc + c.width } +
            (if (enableSelection) selectionColumnWidth else 0.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Table {
            TableHeader {
                TableRow(modifier = Modifier.width(totalWidth)) {
                    if (enableSelection) {
                        TableHead(modifier = Modifier.width(selectionColumnWidth)) {
                            Checkbox(
                                checked = allOnPageSelected,
                                onCheckedChange = { checked ->
                                    val next = selectedKeySet.toMutableSet()
                                    if (checked) {
                                        next.addAll(pageKeys)
                                    } else {
                                        next.removeAll(pageKeys)
                                    }
                                    updateSelection(next)
                                }
                            )
                        }
                    }
                    columns.forEachIndexed { index, col ->
                        val isLast = index == columns.lastIndex
                        TableHead(
                            modifier = Modifier
                                .width(col.width)
                                .then(if (isLast) Modifier.padding(end = 24.dp) else Modifier)
                        ) {
                            SortableHeader(
                                column = col,
                                sort = sort,
                                onClick = {
                                    if (col.sortable && col.comparator != null) {
                                        updateSort(nextSort(sort, col.id))
                                    }
                                }
                            )
                        }
                    }
                }
            }
            TableBody {
                if (pageItems.isEmpty()) {
                    TableRow(
                        modifier = Modifier.width(totalWidth),
                        showBottomBorder = false
                    ) {
                        TableCell(modifier = Modifier.width(totalWidth)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = MaterialTheme.komoStrings.noResults,
                                    color = styles.mutedForeground,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                } else {
                    pageItems.forEachIndexed { index, item ->
                        val key = rowKey(item)
                        val isSelected = key in selectedKeySet
                        TableRow(
                            modifier = Modifier.width(totalWidth),
                            onClick = onRowClick?.let { { it(item) } },
                            selected = isSelected,
                            showBottomBorder = index != pageItems.lastIndex
                        ) {
                            if (enableSelection) {
                                TableCell(modifier = Modifier.width(selectionColumnWidth)) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = { checked ->
                                            val next = selectedKeySet.toMutableSet()
                                            if (checked) {
                                                next.add(key)
                                            } else {
                                                next.remove(key)
                                            }
                                            updateSelection(next)
                                        }
                                    )
                                }
                            }
                            columns.forEachIndexed { ci, col ->
                                val isLast = ci == columns.lastIndex
                                TableCell(
                                    modifier = Modifier
                                        .width(col.width)
                                        .then(if (isLast) Modifier.padding(end = 24.dp) else Modifier)
                                ) {
                                    col.cell(this, item)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (caption != null) {
            TableCaption(text = caption)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (enableSelection) {
                Text(
                    text = MaterialTheme.komoStrings.rowsSelected(selectedKeySet.size, sorted.size),
                    color = styles.mutedForeground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = MaterialTheme.komoStrings.pageIndicator(
                    paginationScope.page + 1,
                    paginationScope.pageCount,
                ),
                color = styles.mutedForeground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        with(paginationScope) { pagination() }
    }
}

@Composable
private fun SortableHeader(
    column: DataTableColumn<*>,
    sort: SortState,
    onClick: () -> Unit
) {
    val styles = MaterialTheme.styles
    val isActive = sort.columnId == column.id && sort.direction != SortDirection.NONE
    val clickable = column.sortable && column.comparator != null

    val rowModifier = if (clickable) {
        Modifier.komoClickable(
            onClick = onClick,
            role = Role.Button,
            stateDescription = when {
                !isActive -> "Not sorted"
                sort.direction == SortDirection.ASC -> "Sorted ascending"
                sort.direction == SortDirection.DESC -> "Sorted descending"
                else -> "Not sorted"
            },
        )
    } else Modifier

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val custom = column.headerContent
        if (custom != null) {
            custom()
        } else {
            Text(
                text = column.header,
                color = styles.mutedForeground,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
        }
        if (clickable) {
            val icon = when {
                !isActive -> Icons.Default.UnfoldMore
                sort.direction == SortDirection.ASC -> Icons.Default.ArrowUpward
                else -> Icons.Default.ArrowDownward
            }
            Icon(
                imageVector = icon,
                contentDescription = "Sort ${column.header}",
                tint = styles.mutedForeground,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

private fun nextSort(current: SortState, columnId: String): SortState {
    return if (current.columnId != columnId) {
        SortState(columnId, SortDirection.ASC)
    } else when (current.direction) {
        SortDirection.NONE -> SortState(columnId, SortDirection.ASC)
        SortDirection.ASC -> SortState(columnId, SortDirection.DESC)
        SortDirection.DESC -> SortState(null, SortDirection.NONE)
    }
}

private fun Modifier.bottomBorder(color: Color): Modifier = this.drawBehind {
    val strokeWidth = 1.dp.toPx()
    val y = size.height - strokeWidth / 2f
    drawLine(
        color = color,
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = strokeWidth
    )
}
