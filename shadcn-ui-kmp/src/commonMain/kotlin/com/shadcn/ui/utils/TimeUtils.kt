package com.shadcn.ui.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal object DateTimeNames {
    val MonthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val ShortMonthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
}


@OptIn(ExperimentalTime::class)
fun YearMonth.Companion.now(): YearMonth {
    val now = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return YearMonth(
        year = now.year,
        month = now.month
    )
}

fun YearMonth.plusMonths(months: Int): YearMonth {
    val date = LocalDate(year, month, 1.coerceIn(1, 28))
    return YearMonth(date.year, date.month)
}

fun YearMonth.minusMonths(months: Int): YearMonth {
    return plusMonths(-months)
}

fun YearMonth.withYear(year: Int): YearMonth = YearMonth(year, month)
fun YearMonth.withMonth(month: Int): YearMonth = YearMonth(year, month)

fun YearMonth.atDay(day: Int): LocalDate = LocalDate(year, month, day)
fun YearMonth.lengthOfMonth(): Int {
    val date = LocalDate(year, month, 1.coerceIn(1, 28))
    val next = date.plus(DatePeriod(months = 1))
    return next.minus(DatePeriod(days = 1)).dayOfYear
}

@OptIn(ExperimentalTime::class)
fun LocalDate.Companion.now(): LocalDate {
    return Clock.System.todayIn(TimeZone.currentSystemDefault())
}
