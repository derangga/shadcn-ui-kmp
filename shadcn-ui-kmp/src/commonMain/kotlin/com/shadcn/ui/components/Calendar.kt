package com.shadcn.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shadcn.ui.themes.ShadcnStyles
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles
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

// Helper data class to represent a YearMonth
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

// Helper function to get short weekday names
private fun DayOfWeek.getShortName(): String {
    return when (this) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }
}

// Helper function to get full month name
private fun Month.getFullName(): String {
    return when (this) {
        Month.JANUARY -> "January"
        Month.FEBRUARY -> "February"
        Month.MARCH -> "March"
        Month.APRIL -> "April"
        Month.MAY -> "May"
        Month.JUNE -> "June"
        Month.JULY -> "July"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "October"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "December"
    }
}

// Helper function to get short month name
private fun Month.getShortName(): String {
    return when (this) {
        Month.JANUARY -> "Jan"
        Month.FEBRUARY -> "Feb"
        Month.MARCH -> "Mar"
        Month.APRIL -> "Apr"
        Month.MAY -> "May"
        Month.JUNE -> "Jun"
        Month.JULY -> "Jul"
        Month.AUGUST -> "Aug"
        Month.SEPTEMBER -> "Sep"
        Month.OCTOBER -> "Oct"
        Month.NOVEMBER -> "Nov"
        Month.DECEMBER -> "Dec"
    }
}

/**
 * A Jetpack Compose Calendar component inspired by Shadcn UI.
 * Allows single date selection and month/year navigation via dropdowns.
 *
 * @param modifier The modifier to be applied to the calendar container.
 * @param selectedDate The currently selected date. Null if no date is selected.
 * @param onDateSelected Callback invoked when a date is selected.
 * @param initialMonth The month to display initially. Defaults to current month.
 * @param dateSelectionMode Defines which dates are clickable (All, PastOrToday, FutureOrToday).
 * @param colors [CalendarStyle] that will be used to resolve the colors used for this calendar in
 */
@OptIn(ExperimentalTime::class)
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    initialMonth: YearMonth = YearMonth.now(),
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors()
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    var currentMonth by remember { mutableStateOf(initialMonth) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    var showMonthPicker by remember { mutableStateOf(false) }
    var showYearPicker by remember { mutableStateOf(false) }

    // Weekday names (e.g., "Sun", "Mon")
    val weekdays = remember {
        listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        ).map { it.getShortName() }
    }

    Box(
        modifier = modifier
            .width(300.dp)
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
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
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
                            .clickable { showMonthPicker = true }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentMonth.month.getShortName(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                ),
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
                            .clickable { showYearPicker = true }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentMonth.year.toString(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                ),
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

                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
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
                        style = TextStyle(
                            color = colors.weekDaysText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Days Grid ---
            val firstDayOfMonth = currentMonth.atDay(1)
            val firstDayOfWeekValue = firstDayOfMonth.dayOfWeek
            val daysInMonth = currentMonth.lengthOfMonth()

            // Calculate leading empty days (Sunday = 0)
            val leadingEmptyDays = when (firstDayOfWeekValue) {
                DayOfWeek.SUNDAY -> 0
                DayOfWeek.MONDAY -> 1
                DayOfWeek.TUESDAY -> 2
                DayOfWeek.WEDNESDAY -> 3
                DayOfWeek.THURSDAY -> 4
                DayOfWeek.FRIDAY -> 5
                DayOfWeek.SATURDAY -> 6
            }

            // Get previous month info for leading dates
            val previousMonth = currentMonth.minusMonths(1)
            val daysInPreviousMonth = previousMonth.lengthOfMonth()

            // Calculate total cells to display (always full weeks)
            val totalActiveDays = leadingEmptyDays + daysInMonth
            val totalCells = ((totalActiveDays + 6) / 7) * 7
            val numRows = totalCells / 7

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

                            val isSelected = date == selectedDate
                            val isToday = date == today

                            // Logic for date clickability based on dateSelectionMode
                            val isClickable = when (dateSelectionMode) {
                                DateSelectionMode.All -> true
                                DateSelectionMode.PastOrToday -> date <= today
                                DateSelectionMode.FutureOrToday -> date >= today
                            }

                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed = interactionSource.collectIsPressedAsState().value
                            val cellBgStyle = colors.dateCellBgStyle
                            val cellTextStyle = colors.dateCellTextStyle
                            val backgroundColor: Color = animateColorAsState(
                                targetValue = when {
                                    isSelected -> cellBgStyle.selectedDate
                                    isClickable && isPressed -> cellBgStyle.onPressed
                                    isToday && isCurrentMonth -> cellBgStyle.todayUnselectedBg
                                    else -> cellBgStyle.defaultDateCell
                                },
                                animationSpec = tween(durationMillis = 100), label = "dayBackground"
                            ).value

                            val textColor = animateColorAsState(
                                targetValue = when {
                                    isSelected -> cellTextStyle.selectedDate
                                    isToday && isCurrentMonth -> cellTextStyle.todayUnselected
                                    isCurrentMonth && isClickable -> cellTextStyle.currentMonthUnselected
                                    isCurrentMonth -> {
                                        when (dateSelectionMode) {
                                            DateSelectionMode.All -> cellTextStyle.currentMonthUnselected
                                            DateSelectionMode.PastOrToday -> {
                                                if (date <= today) {
                                                    cellTextStyle.currentMonthUnselected
                                                } else {
                                                    cellTextStyle.currentMonthDisabled
                                                }
                                            }
                                            DateSelectionMode.FutureOrToday -> {
                                                if (date >= today) {
                                                    cellTextStyle.currentMonthUnselected
                                                } else {
                                                    cellTextStyle.currentMonthDisabled
                                                }
                                            }
                                        }
                                    }
                                    // Previous/next month dates - more muted
                                    isClickable -> cellTextStyle.previousAndNextDateMonth
                                    else -> cellTextStyle.previousAndNextDateMonthDisabled
                                },
                                animationSpec = tween(durationMillis = 100), label = "dayText"
                            ).value

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(radius.sm))
                                    .background(backgroundColor)
                                    .clickable(
                                        enabled = isClickable,
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        onDateSelected(date)
                                        // Optional: Navigate to the selected date's month if it's not current month
                                        if (!isCurrentMonth) {
                                            currentMonth = YearMonth.from(date)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.day.toString(),
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 14.sp,
                                        fontWeight = when {
                                            isSelected -> FontWeight.SemiBold
                                            isToday && isCurrentMonth -> FontWeight.SemiBold
                                            isCurrentMonth -> FontWeight.Normal
                                            else -> FontWeight.Normal
                                        }
                                    )
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
            onMonthSelected = { month ->
                currentMonth = currentMonth.withMonth(month.ordinal + 1)
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
                currentMonth = currentMonth.withYear(year)
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
                    // Calculate the offset to center the item in the viewport
                    // Assuming each item is roughly 44.dp tall (py-2 + text height)
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
                    val backgroundColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedBg else colors.unselectedBg,
                        animationSpec = tween(durationMillis = 100), label = "monthBackground"
                    ).value
                    val textColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedText else colors.unselectedText,
                        animationSpec = tween(durationMillis = 100), label = "monthText"
                    ).value

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
                            text = month.getFullName(),
                            style = TextStyle(
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
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
            val currentYearFromClock = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date.year
            val years = remember { (1970..currentYearFromClock + 5).toList() }
            val listState = rememberLazyListState()

            // Scroll to current year on initial composition
            LaunchedEffect(Unit) {
                val initialIndex = years.indexOf(currentYear)
                if (initialIndex != -1) {
                    // Calculate the offset to center the item in the viewport
                    // Assuming each item is roughly 44.dp tall (py-2 + text height)
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
                    val backgroundColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedBg else colors.unselectedBg,
                        animationSpec = tween(durationMillis = 100), label = "yearBackground"
                    ).value
                    val textColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedText else colors.unselectedText,
                        animationSpec = tween(durationMillis = 100), label = "yearText"
                    ).value

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
                            style = TextStyle(
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

data class CalendarStyle(
    val background: Color,
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
    val defaultDateCell: Color
)

data class DateCellTextStyle(
    val selectedDate: Color,
    val todayUnselected: Color,
    val currentMonthUnselected: Color,
    val currentMonthDisabled: Color,
    val previousAndNextDateMonth: Color,
    val previousAndNextDateMonthDisabled: Color,
)

data class SelectorDialogStyle(
    val selectedBg: Color,
    val selectedText: Color,
    val unselectedBg: Color,
    val unselectedText: Color,
)

object CalendarDefaults {
    @Composable
    private fun colorsFrom(colors: ShadcnStyles): CalendarStyle {
        return CalendarStyle(
            background = colors.background,
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
                defaultDateCell = Color.Transparent
            ),
            dateCellTextStyle = DateCellTextStyle(
                selectedDate = colors.primaryForeground,
                todayUnselected = colors.accentForeground,
                currentMonthUnselected = colors.foreground,
                currentMonthDisabled = colors.mutedForeground.copy(alpha = 0.4f),
                previousAndNextDateMonth = colors.mutedForeground,
                previousAndNextDateMonthDisabled = colors.mutedForeground.copy(alpha = 0.3f)
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
        val styles = MaterialTheme.styles
        return CalendarStyle(
            background = styles.background,
            border = styles.border,
            leftIconTint = styles.foreground,
            rightIconTint = styles.foreground,
            monthText = Color.Unspecified,
            yearText = Color.Unspecified,
            monthSelectorBorder = styles.border,
            yearSelectorBorder = styles.border,
            weekDaysText = styles.mutedForeground,
            dateCellBgStyle = DateCellBackgroundStyle(
                selectedDate = styles.primary,
                todayUnselectedBg = styles.muted,
                onPressed = styles.accent,
                defaultDateCell = Color.Transparent
            ),
            dateCellTextStyle = DateCellTextStyle(
                selectedDate = styles.primaryForeground,
                todayUnselected = styles.accentForeground,
                currentMonthUnselected = styles.foreground,
                currentMonthDisabled = styles.mutedForeground.copy(alpha = 0.4f),
                previousAndNextDateMonth = styles.mutedForeground,
                previousAndNextDateMonthDisabled = styles.mutedForeground.copy(alpha = 0.3f)
            ),
            dialogStyle = SelectorDialogStyle(
                selectedBg = styles.primary,
                selectedText = styles.primaryForeground,
                unselectedBg = Color.Transparent,
                unselectedText = styles.popoverForeground,
            )
        )
    }

    @Composable
    fun colors(overrides: CalendarStyle.() -> CalendarStyle): CalendarStyle {
        val styles = MaterialTheme.styles
        return colorsFrom(styles).overrides()
    }
}