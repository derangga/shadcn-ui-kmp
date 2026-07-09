package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.komoui.themes.radius
import com.komoui.themes.styles
import kotlinx.datetime.LocalDate
import kotlin.math.roundToInt

class DateFormatter(private val pattern: String) {
    /**
     * Formats [date] according to [pattern]. Supported tokens:
     * `yyyy`/`yy` (year), `MMMM`/`MMM`/`MM`/`M` (month), `dd`/`d` (day of month).
     * Any other character is emitted verbatim.
     */
    fun format(date: LocalDate): String {
        val sb = StringBuilder()
        var i = 0
        while (i < pattern.length) {
            val c = pattern[i]
            if (c == 'y' || c == 'M' || c == 'd') {
                var j = i
                while (j < pattern.length && pattern[j] == c) j++
                sb.append(formatToken(c, j - i, date))
                i = j
            } else {
                sb.append(c)
                i++
            }
        }
        return sb.toString()
    }

    private fun formatToken(token: Char, count: Int, date: LocalDate): String {
        // Month.ordinal is 0-based (JANUARY == 0).
        val monthIndex = date.month.ordinal
        return when (token) {
            'y' -> if (count <= 2) (date.year % 100).toString().padStart(2, '0') else date.year.toString()
            'M' -> when {
                count >= 4 -> fullMonths[monthIndex]
                count == 3 -> shortMonths[monthIndex]
                count == 2 -> (monthIndex + 1).toString().padStart(2, '0')
                else -> (monthIndex + 1).toString()
            }
            'd' -> if (count >= 2) date.day.toString().padStart(2, '0') else date.day.toString()
            else -> ""
        }
    }

    companion object {
        private val shortMonths = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        private val fullMonths = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        fun ofPattern(pattern: String): DateFormatter {
            return DateFormatter(pattern)
        }
    }
}

/**
 * A Jetpack Compose Date Picker component for KomoUI.
 * It combines a clickable input field with a popover containing a calendar.
 *
 * @param modifier The modifier to be applied to the date picker container.
 * @param selectedDate The currently selected date. Null if no date is selected.
 * @param dateTimeFormat The format to use for displaying the selected date.
 * @param onDateSelected Callback invoked when a date is selected.
 * @param placeholder The placeholder text to display when no date is selected.
 * @param dateSelectionMode Defines which dates are clickable in the calendar (All, PastOrToday, FutureOrToday).
 * @param colors [CalendarStyle] that will be used to resolve the colors used for this input in
 * @param leadingIcon Optional composable to display at the start of the input field.
 * @param trailingIcon Optional composable to display at the end of the input field.
 */
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    dateTimeFormat: DateFormatter? = null,
    onDateSelected: (LocalDate) -> Unit,
    placeholder: String = "Pick a date",
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    var showCalendarPopup by remember { mutableStateOf(false) }

    var inputHeightPx by remember { mutableIntStateOf(0) }
    var inputXPositionPx by remember { mutableIntStateOf(0) }
    var inputYPositionPx by remember { mutableIntStateOf(0) }

    val formatter = dateTimeFormat ?: DateFormatter.ofPattern("MMM dd, yyyy")
    val formattedDate = selectedDate?.let { formatter.format(it) }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentBorderColor by animateColorAsState(
        targetValue = if (isFocused || isPressed || showCalendarPopup) themeColors.ring else themeColors.border,
        animationSpec = tween(150), label = "datePickerBorderColor"
    )

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onGloballyPositioned { coordinates ->
                    // Get the size and position of the input field
                    inputHeightPx = coordinates.size.height
                    val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                    inputXPositionPx = position?.x?.roundToInt() ?: 0
                    inputYPositionPx = position?.y?.roundToInt() ?: 0
                }
                .clip(RoundedCornerShape(radius.md))
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    showCalendarPopup = !showCalendarPopup
                }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = formattedDate ?: placeholder,
                    color = if (selectedDate != null) themeColors.foreground else themeColors.mutedForeground,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingIcon()
                }

            }
        }

        // Calendar Popup
        if (showCalendarPopup) {
            Popup(
                offset = IntOffset(inputXPositionPx, inputYPositionPx + inputHeightPx),
                properties = PopupProperties(focusable = true),
                onDismissRequest = { showCalendarPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(radius.md))
                        .background(themeColors.popover)
                ) {
                    Calendar(
                        selectionMode = CalendarSelectionMode.Single(
                            selectedDate = selectedDate,
                            onDateSelected = { date ->
                                onDateSelected(date)
                                showCalendarPopup = false
                            }
                        ),
                        initialMonth = selectedDate?.let { YearMonth.from(it) } ?: YearMonth.now(),
                        dateSelectionMode = dateSelectionMode,
                        colors = colors
                    )
                }
            }
        }
    }
}

/**
 * A Jetpack Compose Date Range Picker component for KomoUI.
 * It combines a clickable input field with a popover containing a range-selection calendar.
 *
 * @param modifier The modifier to be applied to the date range picker container.
 * @param selectedRange The currently selected date range. Null if no range is selected.
 * @param dateTimeFormat The format to use for displaying the selected dates.
 * @param onRangeSelected Callback invoked when a complete date range is selected.
 * @param placeholder The placeholder text to display when no range is selected.
 * @param dateSelectionMode Defines which dates are clickable in the calendar (All, PastOrToday, FutureOrToday).
 * @param colors [CalendarStyle] that will be used to resolve the colors used for this input in
 * @param leadingIcon Optional composable to display at the start of the input field.
 * @param trailingIcon Optional composable to display at the end of the input field.
 */
@Composable
fun DateRangePicker(
    modifier: Modifier = Modifier,
    selectedRange: DateRange? = null,
    dateTimeFormat: DateFormatter? = null,
    onRangeSelected: (DateRange) -> Unit,
    placeholder: String = "Pick a date range",
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    var showCalendarPopup by remember { mutableStateOf(false) }

    var inputHeightPx by remember { mutableIntStateOf(0) }
    var inputXPositionPx by remember { mutableIntStateOf(0) }
    var inputYPositionPx by remember { mutableIntStateOf(0) }

    val formatter = dateTimeFormat ?: DateFormatter.ofPattern("MMM dd, yyyy")
    val formattedRange = selectedRange?.let {
        "${formatter.format(it.start)} - ${formatter.format(it.end)}"
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentBorderColor by animateColorAsState(
        targetValue = if (isFocused || isPressed || showCalendarPopup) themeColors.ring else themeColors.border,
        animationSpec = tween(150), label = "dateRangePickerBorderColor"
    )

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onGloballyPositioned { coordinates ->
                    inputHeightPx = coordinates.size.height
                    val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                    inputXPositionPx = position?.x?.roundToInt() ?: 0
                    inputYPositionPx = position?.y?.roundToInt() ?: 0
                }
                .clip(RoundedCornerShape(radius.md))
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    showCalendarPopup = !showCalendarPopup
                }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = formattedRange ?: placeholder,
                    color = if (selectedRange != null) themeColors.foreground else themeColors.mutedForeground,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingIcon()
                }
            }
        }

        // Calendar Popup
        if (showCalendarPopup) {
            Popup(
                offset = IntOffset(inputXPositionPx, inputYPositionPx + inputHeightPx),
                properties = PopupProperties(focusable = true),
                onDismissRequest = { showCalendarPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(radius.md))
                        .background(themeColors.popover)
                ) {
                    Calendar(
                        selectionMode = CalendarSelectionMode.Range(
                            selectedRange = selectedRange,
                            onRangeSelected = { range ->
                                onRangeSelected(range)
                                showCalendarPopup = false
                            }
                        ),
                        initialMonth = selectedRange?.start?.let { YearMonth.from(it) } ?: YearMonth.now(),
                        dateSelectionMode = dateSelectionMode,
                        colors = colors
                    )
                }
            }
        }
    }
}