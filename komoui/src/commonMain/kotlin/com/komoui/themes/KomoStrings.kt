package com.komoui.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Localizable UI strings for KomoUI components (labels, accessibility descriptions, calendar
 * names). Override via [KomoTheme]'s `strings` parameter — English defaults keep existing
 * callers working unchanged. Read within components via `MaterialTheme.komoStrings`.
 *
 * Month/weekday name lists are indexed by the corresponding `ordinal`: months JANUARY(0)..
 * DECEMBER(11), weekdays MONDAY(0)..SUNDAY(6).
 */
data class KomoStrings(
    val closeDialog: String = "Close dialog",
    val close: String = "Close",
    val toggleSidebar: String = "Toggle Sidebar",
    val expanded: String = "Expanded",
    val collapsed: String = "Collapsed",
    val previous: String = "Previous",
    val next: String = "Next",
    val noResults: String = "No results.",
    val noResultsFound: String = "No results found.",
    val searchPlaceholder: String = "Search options...",
    val rowsSelected: (selected: Int, total: Int) -> String = { selected, total ->
        "$selected of $total row(s) selected."
    },
    val pageIndicator: (page: Int, pageCount: Int) -> String = { page, pageCount ->
        "Page $page of $pageCount"
    },
    val monthNamesFull: List<String> = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December",
    ),
    val monthNamesShort: List<String> = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
    ),
    val weekdayNamesShort: List<String> = listOf(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun",
    ),
)

internal val LocalKomoStrings = staticCompositionLocalOf { KomoStrings() }

val MaterialTheme.komoStrings: KomoStrings
    @Composable
    @ReadOnlyComposable
    get() = LocalKomoStrings.current
