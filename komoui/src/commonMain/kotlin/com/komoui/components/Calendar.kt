package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.komoui.themes.KomoStyles
import com.komoui.themes.radius
import com.komoui.themes.komoStrings
import com.komoui.themes.komoTypography
import com.komoui.themes.styles
import com.komoui.utils.komoSelectable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

enum class DateSelectionMode {
    /** Allows selection of all dates. */
    All,
    /** Allows selection of today's date and past dates. */
    PastOrToday,
    /** Allows selection of today's date and future dates. */
    FutureOrToday
}

/**
 * Represents a date range with a start and end date.
 */
data class DateRange(val start: LocalDate, val end: LocalDate)

/**
 * Defines the selection mode for the Calendar component.
 */
sealed interface CalendarSelectionMode {
    /**
     * Single date selection mode.
     *
     * @param selectedDate The currently selected date, or null if none.
     * @param onDateSelected Callback invoked when a date is selected.
     */
    data class Single(
        val selectedDate: LocalDate? = null,
        val onDateSelected: (LocalDate) -> Unit
    ) : CalendarSelectionMode

    /**
     * Range date selection mode. First click sets start, second click sets end.
     * Clicking after a complete range resets and starts a new selection.
     *
     * @param selectedRange The currently selected date range, or null if none.
     * @param onRangeSelected Callback invoked when a complete range is selected.
     */
    data class Range(
        val selectedRange: DateRange? = null,
        val onRangeSelected: (DateRange) -> Unit
    ) : CalendarSelectionMode
}

/**
 * Helper data class to represent a year-month pair for calendar navigation.
 */
data class YearMonth(val year: Int, val month: Month) {
    fun atDay(day: Int): LocalDate {
        return LocalDate(year, month, day)
    }

    fun minusMonths(months: Long): YearMonth {
        var newYear = year
        var newMonth = month.ordinal + 1 // Month is 1-based
        newMonth -= months.toInt()
        while (newMonth <= 0) {
            newMonth += 12
            newYear--
        }
        return YearMonth(newYear, Month(newMonth))
    }

    fun plusMonths(months: Long): YearMonth {
        var newYear = year
        var newMonth = month.ordinal + 1 // Month is 1-based
        newMonth += months.toInt()
        while (newMonth > 12) {
            newMonth -= 12
            newYear++
        }
        return YearMonth(newYear, Month(newMonth))
    }

    fun lengthOfMonth(): Int {
        return month.length(isLeapYear(year))
    }

    fun withMonth(monthValue: Int): YearMonth {
        return YearMonth(year, Month(monthValue))
    }

    fun withYear(newYear: Int): YearMonth {
        return YearMonth(newYear, month)
    }

    companion object {
        @OptIn(ExperimentalTime::class)
        fun now(): YearMonth {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            return YearMonth(today.year, today.month)
        }

        fun from(date: LocalDate): YearMonth {
            return YearMonth(date.year, date.month)
        }
    }
}

// Helper function to check if a year is a leap year
private fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

// Helper function to get month length
private fun Month.length(isLeapYear: Boolean): Int {
    return when (this) {
        Month.FEBRUARY -> if (isLeapYear) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}

/**
 * Converts a [DayOfWeek] to a Sunday-start index (Sunday=0, Monday=1, ..., Saturday=6).
 */
private fun DayOfWeek.toSundayStartIndex(): Int = when (this) {
    DayOfWeek.SUNDAY -> 0
    DayOfWeek.MONDAY -> 1
    DayOfWeek.TUESDAY -> 2
    DayOfWeek.WEDNESDAY -> 3
    DayOfWeek.THURSDAY -> 4
    DayOfWeek.FRIDAY -> 5
    DayOfWeek.SATURDAY -> 6
}

/**
 * A Jetpack Compose Calendar component for KomoUI.
 * Backward-compatible overload that wraps single date selection into [CalendarSelectionMode.Single].
 *
 * @param modifier The modifier to be applied to the calendar container.
 * @param selectedDate The currently selected date. Null if no date is selected.
 * @param onDateSelected Callback invoked when a date is selected.
 * @param initialMonth The month to display initially in uncontrolled mode. Defaults to current month.
 * @param month Controlled displayed month. Pass non-null (with [onMonthChange]) to drive navigation externally.
 * @param onMonthChange Called when the displayed month changes (via arrows, pickers, or cross-month day taps).
 * @param dateSelectionMode Defines which dates are clickable (All, PastOrToday, FutureOrToday).
 * @param colors [CalendarStyle] that will be used to resolve the colors used for this calendar in
 */
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    initialMonth: YearMonth = YearMonth.now(),
    month: YearMonth? = null,
    onMonthChange: ((YearMonth) -> Unit)? = null,
    enabled: Boolean = true,
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors()
) {
    Calendar(
        modifier = modifier,
        selectionMode = CalendarSelectionMode.Single(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        ),
        initialMonth = initialMonth,
        month = month,
        onMonthChange = onMonthChange,
        enabled = enabled,
        dateSelectionMode = dateSelectionMode,
        colors = colors
    )
}

/**
 * A Jetpack Compose Calendar component for KomoUI.
 * Supports single date selection and range date selection via [CalendarSelectionMode].
 *
 * @param modifier The modifier to be applied to the calendar container.
 * @param selectionMode The selection mode (single or range).
 * @param initialMonth The month to display initially in uncontrolled mode. Defaults to current month.
 * @param month Controlled displayed month. Pass non-null (with [onMonthChange]) to drive navigation externally.
 * @param onMonthChange Called when the displayed month changes (via arrows, pickers, or cross-month day taps).
 * @param dateSelectionMode Defines which dates are clickable (All, PastOrToday, FutureOrToday).
 * @param colors [CalendarStyle] that will be used to resolve the colors used for this calendar in
 */
@OptIn(ExperimentalTime::class)
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectionMode: CalendarSelectionMode,
    initialMonth: YearMonth = YearMonth.now(),
    month: YearMonth? = null,
    onMonthChange: ((YearMonth) -> Unit)? = null,
    enabled: Boolean = true,
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors()
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val strings = MaterialTheme.komoStrings
    // Controlled ([month] non-null) or uncontrolled (seeded by [initialMonth]) month navigation.
    var internalMonth by remember(initialMonth) { mutableStateOf(initialMonth) }
    val currentMonth = month ?: internalMonth
    val setMonth: (YearMonth) -> Unit = { next ->
        if (month == null) internalMonth = next
        onMonthChange?.invoke(next)
    }
    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    var showMonthPicker by remember { mutableStateOf(false) }
    var showYearPicker by remember { mutableStateOf(false) }

    // Range selection internal state
    var rangeStart by remember { mutableStateOf<LocalDate?>(null) }
    var rangeEnd by remember { mutableStateOf<LocalDate?>(null) }
    var rangeComplete by remember { mutableStateOf(false) }

    // Sync internal range state with external selectedRange
    if (selectionMode is CalendarSelectionMode.Range) {
        LaunchedEffect(selectionMode.selectedRange) {
            if (selectionMode.selectedRange != null) {
                rangeStart = selectionMode.selectedRange.start
                rangeEnd = selectionMode.selectedRange.end
                rangeComplete = true
            } else {
                rangeStart = null
                rangeEnd = null
                rangeComplete = false
            }
        }
    }

    // Weekday names (e.g., "Sun", "Mon")
    val weekdays = remember(strings) {
        listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        ).map { strings.weekdayNamesShort[it.ordinal] }
    }

    // Text styles used in the day grid
    val dayTextStyleNormal = MaterialTheme.komoTypography.body
    val dayTextStyleBold = MaterialTheme.komoTypography.body.copy(fontWeight = FontWeight.SemiBold)

    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .widthIn(max = 300.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .border(1.dp, colors.border, RoundedCornerShape(radius.lg))
                .padding(8.dp)
        ) {
            // --- Header: Month, Year and Navigation Arrows ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { setMonth(currentMonth.minusMonths(1)) }, enabled = enabled) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = colors.leftIconTint
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Month Selector
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(radius.md))
                            .border(1.dp, colors.monthSelectorBorder, RoundedCornerShape(radius.md))
                            .clickable(enabled = enabled) { showMonthPicker = true }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = strings.monthNamesShort[currentMonth.month.ordinal],
                                style = MaterialTheme.komoTypography.titleMedium,
                                color = colors.monthText
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Month",
                                tint = themeColors.foreground,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Year Selector
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(radius.md))
                            .border(1.dp, colors.yearSelectorBorder, RoundedCornerShape(radius.md))
                            .clickable(enabled = enabled) { showYearPicker = true }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentMonth.year.toString(),
                                style = MaterialTheme.komoTypography.titleMedium,
                                color = colors.yearText
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Year",
                                tint = themeColors.foreground,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                IconButton(onClick = { setMonth(currentMonth.plusMonths(1)) }, enabled = enabled) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = colors.rightIconTint
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Weekdays ---
            Row(modifier = Modifier.fillMaxWidth()) {
                weekdays.forEach { weekday ->
                    Text(
                        text = weekday,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.komoTypography.labelMedium.copy(
                            color = colors.weekDaysText
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Days Grid ---
            val firstDayOfMonth = currentMonth.atDay(1)
            val daysInMonth = currentMonth.lengthOfMonth()

            // Calculate leading empty days using Sunday-start index
            val leadingEmptyDays = firstDayOfMonth.dayOfWeek.toSundayStartIndex()

            // Get previous month info for leading dates
            val previousMonth = currentMonth.minusMonths(1)
            val daysInPreviousMonth = previousMonth.lengthOfMonth()

            // Calculate total cells to display (always full weeks)
            val totalActiveDays = leadingEmptyDays + daysInMonth
            val totalCells = ((totalActiveDays + 6) / 7) * 7
            val numRows = totalCells / 7

            val cellBgStyle = colors.dateCellBgStyle
            val cellTextStyle = colors.dateCellTextStyle

            Column {
                for (row in 0 until numRows) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (col in 0 until 7) {
                            val cellIndex = row * 7 + col

                            val (date, isCurrentMonth) = when {
                                // Leading days from previous month
                                cellIndex < leadingEmptyDays -> {
                                    val dayOfMonth = daysInPreviousMonth - (leadingEmptyDays - cellIndex - 1)
                                    Pair(previousMonth.atDay(dayOfMonth), false)
                                }
                                // Current month days
                                cellIndex < leadingEmptyDays + daysInMonth -> {
                                    val dayOfMonth = cellIndex - leadingEmptyDays + 1
                                    Pair(currentMonth.atDay(dayOfMonth), true)
                                }
                                // Trailing days from next month
                                else -> {
                                    val dayOfMonth = cellIndex - leadingEmptyDays - daysInMonth + 1
                                    val nextMonth = currentMonth.plusMonths(1)
                                    Pair(nextMonth.atDay(dayOfMonth), false)
                                }
                            }

                            // Determine selection state based on mode
                            val isSelected = when (selectionMode) {
                                is CalendarSelectionMode.Single -> date == selectionMode.selectedDate
                                is CalendarSelectionMode.Range -> false // handled separately
                            }

                            // Range state calculations
                            val isRangeStart = selectionMode is CalendarSelectionMode.Range &&
                                    rangeStart != null && date == rangeStart
                            val isRangeEnd = selectionMode is CalendarSelectionMode.Range &&
                                    rangeEnd != null && date == rangeEnd
                            val isInRange = selectionMode is CalendarSelectionMode.Range &&
                                    rangeStart != null && rangeEnd != null &&
                                    date > rangeStart!! && date < rangeEnd!!
                            val isSingleDayRange = isRangeStart && isRangeEnd

                            val isToday = date == today

                            // Logic for date clickability based on dateSelectionMode
                            val isClickable = enabled && when (dateSelectionMode) {
                                DateSelectionMode.All -> true
                                DateSelectionMode.PastOrToday -> date <= today
                                DateSelectionMode.FutureOrToday -> date >= today
                            }

                            val interactionSource = remember(date) { MutableInteractionSource() }
                            val isPressed = interactionSource.collectIsPressedAsState().value

                            // Determine shape based on range position
                            val shape = when {
                                selectionMode is CalendarSelectionMode.Range && (isRangeStart || isRangeEnd || isInRange) -> {
                                    when {
                                        isSingleDayRange -> RoundedCornerShape(radius.sm)
                                        isRangeStart -> RoundedCornerShape(
                                            topStart = radius.sm,
                                            bottomStart = radius.sm,
                                            topEnd = 0.dp,
                                            bottomEnd = 0.dp
                                        )
                                        isRangeEnd -> RoundedCornerShape(
                                            topStart = 0.dp,
                                            bottomStart = 0.dp,
                                            topEnd = radius.sm,
                                            bottomEnd = radius.sm
                                        )
                                        isInRange -> RoundedCornerShape(0.dp)
                                        else -> RoundedCornerShape(radius.sm)
                                    }
                                }
                                else -> RoundedCornerShape(radius.sm)
                            }

                            // Hoisted out of the isPressed branch so the animation persists across
                            // press/release and actually animates (instead of snapping and disposing).
                            val targetBackground = when {
                                isPressed && isClickable -> cellBgStyle.onPressed
                                isSelected -> cellBgStyle.selectedDate
                                isSingleDayRange -> cellBgStyle.rangeEndpointBg
                                isRangeStart || isRangeEnd -> cellBgStyle.rangeEndpointBg
                                isInRange -> cellBgStyle.inRangeBg
                                isToday && isCurrentMonth -> cellBgStyle.todayUnselectedBg
                                else -> cellBgStyle.defaultDateCell
                            }
                            // Avoid animateColorAsState fading through black: Color.Transparent is
                            // (0,0,0, alpha 0), so tweening a transparent cell to a light fill (in-range
                            // accent) passes through muddy grey. Swap a transparent target for a same-hue
                            // zero-alpha color so transparent<->fill transitions animate alpha only.
                            val animTarget = if (targetBackground.alpha == 0f)
                                themeColors.accent.copy(alpha = 0f) else targetBackground
                            val backgroundColor = animateColorAsState(
                                targetValue = animTarget,
                                animationSpec = tween(durationMillis = 100),
                                label = "dayBackground"
                            ).value

                            val textColor = when {
                                isSelected -> cellTextStyle.selectedDate
                                isSingleDayRange -> cellTextStyle.rangeEndpointText
                                isRangeStart || isRangeEnd -> cellTextStyle.rangeEndpointText
                                isInRange -> cellTextStyle.inRangeText
                                isToday && isCurrentMonth -> cellTextStyle.todayUnselected
                                isCurrentMonth && isClickable -> cellTextStyle.currentMonthUnselected
                                isCurrentMonth -> {
                                    when (dateSelectionMode) {
                                        DateSelectionMode.All -> cellTextStyle.currentMonthUnselected
                                        DateSelectionMode.PastOrToday -> {
                                            if (date <= today) cellTextStyle.currentMonthUnselected
                                            else cellTextStyle.currentMonthDisabled
                                        }
                                        DateSelectionMode.FutureOrToday -> {
                                            if (date >= today) cellTextStyle.currentMonthUnselected
                                            else cellTextStyle.currentMonthDisabled
                                        }
                                    }
                                }
                                isClickable -> cellTextStyle.previousAndNextDateMonth
                                else -> cellTextStyle.previousAndNextDateMonthDisabled
                            }

                            val isRangeHighlighted = isRangeStart || isRangeEnd || isInRange
                            val textStyle = if (isSelected || isRangeHighlighted || (isToday && isCurrentMonth)) {
                                dayTextStyleBold
                            } else {
                                dayTextStyleNormal
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(shape)
                                    .background(backgroundColor)
                                    .komoSelectable(
                                        selected = isSelected,
                                        enabled = isClickable,
                                        role = Role.Button,
                                        shape = shape,
                                        interactionSource = interactionSource,
                                        onClick = {
                                        when (selectionMode) {
                                            is CalendarSelectionMode.Single -> {
                                                selectionMode.onDateSelected(date)
                                            }
                                            is CalendarSelectionMode.Range -> {
                                                if (rangeComplete || rangeStart == null) {
                                                    // Start new range
                                                    rangeStart = date
                                                    rangeEnd = date
                                                    rangeComplete = false
                                                } else {
                                                    // Complete the range
                                                    val start = rangeStart!!
                                                    if (date < start) {
                                                        // Auto-swap: clicked date becomes start
                                                        rangeEnd = start
                                                        rangeStart = date
                                                    } else {
                                                        rangeEnd = date
                                                    }
                                                    rangeComplete = true
                                                    selectionMode.onRangeSelected(
                                                        DateRange(rangeStart!!, rangeEnd!!)
                                                    )
                                                }
                                            }
                                        }
                                        // Navigate to the selected date's month if it's not current month
                                        if (!isCurrentMonth) {
                                            setMonth(YearMonth.from(date))
                                        }
                                        },
                                    )
                                    .semantics { contentDescription = date.toString() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.day.toString(),
                                    style = textStyle,
                                    color = textColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Month Picker Dialog
    if (showMonthPicker) {
        MonthPickerDialog(
            currentMonth = currentMonth.month,
            onMonthSelected = { selected ->
                setMonth(currentMonth.withMonth(selected.ordinal + 1))
                showMonthPicker = false
            },
            onDismissRequest = { showMonthPicker = false },
            colors = colors.dialogStyle
        )
    }

    // Year Picker Dialog
    if (showYearPicker) {
        YearPickerDialog(
            currentYear = currentMonth.year,
            onYearSelected = { year ->
                setMonth(currentMonth.withYear(year))
                showYearPicker = false
            },
            onDismissRequest = { showYearPicker = false },
            colors = colors.dialogStyle
        )
    }
}

/**
 * Dialog for selecting a month.
 */
@Composable
private fun MonthPickerDialog(
    currentMonth: Month,
    onMonthSelected: (Month) -> Unit,
    onDismissRequest: () -> Unit,
    colors: SelectorDialogStyle
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val density = LocalDensity.current

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(themeColors.popover, RoundedCornerShape(radius.lg))
                .border(1.dp, themeColors.border, RoundedCornerShape(radius.lg))
                .padding(8.dp)
                .height(300.dp)
        ) {
            val months = remember { Month.entries }
            val listState = rememberLazyListState()

            // Scroll to current month on initial composition
            LaunchedEffect(Unit) {
                val initialIndex = months.indexOf(currentMonth)
                if (initialIndex != -1) {
                    val itemHeightPx = with(density) { 44.dp.toPx() }
                    val containerHeightPx = with(density) { 300.dp.toPx() }
                    val offsetToCenter = (itemHeightPx / 2f) - (containerHeightPx / 2f)
                    listState.scrollToItem(initialIndex, offsetToCenter.roundToInt())
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(months) { month ->
                    val isSelected = month == currentMonth
                    val backgroundColor = if (isSelected) colors.selectedBg else colors.unselectedBg
                    val textColor = if (isSelected) colors.selectedText else colors.unselectedText

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(radius.sm))
                            .background(backgroundColor)
                            .clickable { onMonthSelected(month) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = MaterialTheme.komoStrings.monthNamesFull[month.ordinal],
                            style = MaterialTheme.komoTypography.titleMedium.copy(
                                color = textColor
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dialog for selecting a year.
 */
@OptIn(ExperimentalTime::class)
@Composable
private fun YearPickerDialog(
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    colors: SelectorDialogStyle
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val density = LocalDensity.current

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(themeColors.popover, RoundedCornerShape(radius.lg))
                .border(1.dp, themeColors.border, RoundedCornerShape(radius.lg))
                .padding(8.dp)
                .height(300.dp)
        ) {
            val years = remember {
                val now = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date.year
                (1970..now + 5).toList()
            }
            val listState = rememberLazyListState()

            // Scroll to current year on initial composition
            LaunchedEffect(Unit) {
                val initialIndex = years.indexOf(currentYear)
                if (initialIndex != -1) {
                    val itemHeightPx = with(density) { 44.dp.toPx() }
                    val containerHeightPx = with(density) { 300.dp.toPx() }
                    val offsetToCenter = (itemHeightPx / 2f) - (containerHeightPx / 2f)
                    listState.scrollToItem(initialIndex, offsetToCenter.roundToInt())
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(years) { year ->
                    val isSelected = year == currentYear
                    val backgroundColor = if (isSelected) colors.selectedBg else colors.unselectedBg
                    val textColor = if (isSelected) colors.selectedText else colors.unselectedText

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(radius.sm))
                            .background(backgroundColor)
                            .clickable { onYearSelected(year) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.komoTypography.titleMedium.copy(
                                color = textColor
                            )
                        )
                    }
                }
            }
        }
    }
}

data class CalendarStyle(
    val border: Color,
    val leftIconTint: Color,
    val rightIconTint: Color,
    val monthText: Color,
    val yearText: Color,
    val monthSelectorBorder: Color,
    val yearSelectorBorder: Color,
    val weekDaysText: Color,
    val dateCellBgStyle: DateCellBackgroundStyle,
    val dateCellTextStyle: DateCellTextStyle,
    val dialogStyle: SelectorDialogStyle
)

data class DateCellBackgroundStyle(
    val selectedDate: Color,
    val todayUnselectedBg: Color,
    val onPressed: Color,
    val defaultDateCell: Color,
    val rangeEndpointBg: Color = Color.Unspecified,
    val inRangeBg: Color = Color.Unspecified
)

data class DateCellTextStyle(
    val selectedDate: Color,
    val todayUnselected: Color,
    val currentMonthUnselected: Color,
    val currentMonthDisabled: Color,
    val previousAndNextDateMonth: Color,
    val previousAndNextDateMonthDisabled: Color,
    val rangeEndpointText: Color = Color.Unspecified,
    val inRangeText: Color = Color.Unspecified
)

data class SelectorDialogStyle(
    val selectedBg: Color,
    val selectedText: Color,
    val unselectedBg: Color,
    val unselectedText: Color,
)

object CalendarDefaults {
    @Composable
    private fun colorsFrom(colors: KomoStyles): CalendarStyle {
        return CalendarStyle(
            border = colors.border,
            leftIconTint = colors.foreground,
            rightIconTint = colors.foreground,
            monthText = Color.Unspecified,
            yearText = Color.Unspecified,
            monthSelectorBorder = colors.border,
            yearSelectorBorder = colors.border,
            weekDaysText = colors.mutedForeground,
            dateCellBgStyle = DateCellBackgroundStyle(
                selectedDate = colors.primary,
                todayUnselectedBg = colors.muted,
                onPressed = colors.accent,
                defaultDateCell = Color.Transparent,
                rangeEndpointBg = colors.primary,
                inRangeBg = colors.accent
            ),
            dateCellTextStyle = DateCellTextStyle(
                selectedDate = colors.primaryForeground,
                todayUnselected = colors.accentForeground,
                currentMonthUnselected = colors.foreground,
                currentMonthDisabled = colors.mutedForeground.copy(alpha = 0.4f),
                previousAndNextDateMonth = colors.mutedForeground,
                previousAndNextDateMonthDisabled = colors.mutedForeground.copy(alpha = 0.3f),
                rangeEndpointText = colors.primaryForeground,
                inRangeText = colors.accentForeground
            ),
            dialogStyle = SelectorDialogStyle(
                selectedBg = colors.primary,
                selectedText = colors.primaryForeground,
                unselectedBg = Color.Transparent,
                unselectedText = colors.popoverForeground,
            )
        )
    }

    @Composable
    fun colors(): CalendarStyle {
        return colorsFrom(MaterialTheme.styles)
    }

    @Composable
    fun colors(overrides: CalendarStyle.() -> CalendarStyle): CalendarStyle {
        return colorsFrom(MaterialTheme.styles).overrides()
    }
}
