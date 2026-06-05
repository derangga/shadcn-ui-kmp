package com.komoui.kmp

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

actual fun LocalDateTime.format(
    format: String
): String = date.format(format.replaceTimeTokens(this))

actual fun LocalDate.format(
    format: String
): String {
    val replacements = listOf(
        "yyyy" to year.toString().padStart(4, '0'),
        "MMMM" to month.fullName,
        "MMM" to month.shortName,
        "MM" to month.number.toString().padStart(2, '0'),
        "M" to month.number.toString(),
        "dd" to day.toString().padStart(2, '0'),
        "d" to day.toString()
    )

    return replacements.fold(format) { result, (token, value) ->
        result.replace(token, value)
    }
}

private fun String.replaceTimeTokens(dateTime: LocalDateTime): String {
    val hour24 = dateTime.hour
    val hour12 = when (val hour = hour24 % 12) {
        0 -> 12
        else -> hour
    }
    val replacements = listOf(
        "HH" to hour24.toString().padStart(2, '0'),
        "H" to hour24.toString(),
        "hh" to hour12.toString().padStart(2, '0'),
        "h" to hour12.toString(),
        "mm" to dateTime.minute.toString().padStart(2, '0'),
        "m" to dateTime.minute.toString(),
        "ss" to dateTime.second.toString().padStart(2, '0'),
        "s" to dateTime.second.toString()
    )

    return replacements.fold(this) { result, (token, value) ->
        result.replace(token, value)
    }
}

private val Month.number: Int
    get() = ordinal + 1

private val Month.shortName: String
    get() = when (this) {
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

private val Month.fullName: String
    get() = when (this) {
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
