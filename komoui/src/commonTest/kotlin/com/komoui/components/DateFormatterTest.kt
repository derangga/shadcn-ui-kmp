package com.komoui.components

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class DateFormatterTest {

    private val expectedShort = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    @Test
    fun allMonthsFormatWithCorrectShortName() {
        val formatter = DateFormatter.ofPattern("MMM dd, yyyy")
        for (month in 1..12) {
            val date = LocalDate(2024, month, 5)
            assertEquals("${expectedShort[month - 1]} 05, 2024", formatter.format(date))
        }
    }

    @Test
    fun januaryDoesNotCrashAndShowsJan() {
        val formatter = DateFormatter.ofPattern("MMM dd, yyyy")
        assertEquals("Jan 01, 2024", formatter.format(LocalDate(2024, 1, 1)))
    }

    @Test
    fun patternIsHonored() {
        val date = LocalDate(2024, 3, 9)
        assertEquals("2024-03-09", DateFormatter.ofPattern("yyyy-MM-dd").format(date))
        assertEquals("March 9", DateFormatter.ofPattern("MMMM d").format(date))
        assertEquals("09/03/24", DateFormatter.ofPattern("dd/MM/yy").format(date))
    }
}
