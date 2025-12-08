package com.shadcn.ui.kmp

import kotlinx.datetime.LocalDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDate

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateComponents
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
actual fun LocalDateTime.format(format: String): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = format
    return dateFormatter.stringFromDate(
        toInstant(TimeZone.currentSystemDefault()).toNSDate()
    )
}

@OptIn(ExperimentalTime::class)
actual fun LocalDate.format(format: String): String {
    val dateComponents = NSDateComponents().also {
        it.year = year.toLong()
        it.month = month.ordinal.toLong()
        it.day = day.toLong()
    }

    val calendar = NSCalendar.currentCalendar
    val date = calendar.dateFromComponents(dateComponents) ?: return toString()

    val formatter = NSDateFormatter().apply {
        dateFormat = format
    }

    return formatter.stringFromDate(date)
}