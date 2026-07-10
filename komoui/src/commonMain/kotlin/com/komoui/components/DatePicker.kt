package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.komoui.themes.radius
import com.komoui.themes.KomoFieldDefaults
import com.komoui.themes.styles
import com.komoui.utils.komoClickable
import com.komoui.utils.rememberAnchoredPopupPositionProvider
import kotlinx.datetime.LocalDate

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

@Composable
fun DatePicker(
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    dateTimeFormat: DateFormatter? = null,
    placeholder: String = "Pick a date",
    enabled: Boolean = true,
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val formatter = dateTimeFormat ?: DateFormatter.ofPattern("MMM dd, yyyy")
    val formattedDate = selectedDate?.let { formatter.format(it) }

    DatePickerScaffold(
        modifier = modifier,
        displayText = formattedDate,
        hasValue = selectedDate != null,
        placeholder = placeholder,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    ) { onDismiss ->
        Calendar(
            selectionMode = CalendarSelectionMode.Single(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    onDateSelected(date)
                    onDismiss()
                }
            ),
            initialMonth = selectedDate?.let { YearMonth.from(it) } ?: YearMonth.now(),
            dateSelectionMode = dateSelectionMode,
            colors = colors
        )
    }
}

@Composable
fun DateRangePicker(
    onRangeSelected: (DateRange) -> Unit,
    modifier: Modifier = Modifier,
    selectedRange: DateRange? = null,
    dateTimeFormat: DateFormatter? = null,
    placeholder: String = "Pick a date range",
    enabled: Boolean = true,
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val formatter = dateTimeFormat ?: DateFormatter.ofPattern("MMM dd, yyyy")
    val formattedRange = selectedRange?.let {
        "${formatter.format(it.start)} - ${formatter.format(it.end)}"
    }

    DatePickerScaffold(
        modifier = modifier,
        displayText = formattedRange,
        hasValue = selectedRange != null,
        placeholder = placeholder,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    ) { onDismiss ->
        Calendar(
            selectionMode = CalendarSelectionMode.Range(
                selectedRange = selectedRange,
                onRangeSelected = { range ->
                    onRangeSelected(range)
                    onDismiss()
                }
            ),
            initialMonth = selectedRange?.start?.let { YearMonth.from(it) } ?: YearMonth.now(),
            dateSelectionMode = dateSelectionMode,
            colors = colors
        )
    }
}

/**
 * Shared trigger + popup scaffold behind [DatePicker] and [DateRangePicker]. Renders the bordered
 * input field showing [displayText] (or [placeholder]) and hosts [popover] in an anchored popup,
 * passing it a dismiss callback to close after a selection.
 */
@Composable
private fun DatePickerScaffold(
    modifier: Modifier,
    displayText: String?,
    hasValue: Boolean,
    placeholder: String,
    enabled: Boolean,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    popover: @Composable (onDismiss: () -> Unit) -> Unit,
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    var showCalendarPopup by remember { mutableStateOf(false) }
    val positionProvider = rememberAnchoredPopupPositionProvider()

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentBorderColor by animateColorAsState(
        targetValue = if (isFocused || isPressed || showCalendarPopup) themeColors.ring else themeColors.border,
        animationSpec = tween(150), label = "datePickerBorderColor"
    )

    Column(modifier = modifier.alpha(if (enabled) 1f else 0.5f)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(KomoFieldDefaults.Height)
                .clip(RoundedCornerShape(radius.md))
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md))
                .komoClickable(
                    onClick = { showCalendarPopup = !showCalendarPopup },
                    enabled = enabled,
                    role = Role.DropdownList,
                    shape = RoundedCornerShape(radius.md),
                    stateDescription = if (showCalendarPopup) "Expanded" else "Collapsed",
                    interactionSource = interactionSource,
                )
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
                    text = displayText ?: placeholder,
                    color = if (hasValue) themeColors.foreground else themeColors.mutedForeground,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingIcon()
                }
            }
        }

        if (showCalendarPopup) {
            Popup(
                popupPositionProvider = positionProvider,
                properties = PopupProperties(focusable = true),
                onDismissRequest = { showCalendarPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(radius.md))
                        .background(themeColors.popover)
                ) {
                    popover { showCalendarPopup = false }
                }
            }
        }
    }
}