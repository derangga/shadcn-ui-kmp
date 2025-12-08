package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Calendar
import com.shadcn.ui.components.CalendarDefaults
import com.shadcn.ui.components.DateSelectionMode
import com.shadcn.ui.kmp.format
import com.shadcn.ui.themes.styles
import com.shadcn.ui.utils.now
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout
import kotlinx.datetime.LocalDate

@Composable
fun CalendarPage() {
    var selectedDateAll by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var customDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var selectedDatePast by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var selectedDateFuture by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    Layout {
        Text(
            "Calendar",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "A date field component that allows users to enter and edit date.",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Basic calendar usage") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Calendar(
                    selectedDate = selectedDateAll,
                    onDateSelected = { date ->
                        selectedDateAll = date
                    },
                )

                Text(
                    text = "Selected Date (All): ${
                        selectedDateAll?.format("MMM dd, yyyy") ?: "None"
                    }",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        ContentPageWithTitle("2. Calendar with date past or today") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Calendar(
                    selectedDate = selectedDatePast,
                    onDateSelected = { date ->
                        selectedDatePast = date
                    },
                    dateSelectionMode = DateSelectionMode.PastOrToday
                )

                Text(
                    text = "Selected Date (Past): ${
                        selectedDatePast?.format("MMM dd, yyyy") ?: "None"
                    }",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        ContentPageWithTitle("3. Calendar with date future or today") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Calendar(
                    selectedDate = selectedDateFuture,
                    onDateSelected = { date ->
                        selectedDateFuture = date
                    },
                    dateSelectionMode = DateSelectionMode.FutureOrToday
                )

                Text(
                    text = "Selected Date (Future): ${
                        selectedDateFuture?.format("MMM dd, yyyy") ?: "None"
                    }",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        ContentPageWithTitle("4. Custom calendar color") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val chart3 = MaterialTheme.styles.chart3
                val chart4 = MaterialTheme.styles.chart4
                Calendar(
                    selectedDate = customDate,
                    onDateSelected = { date ->
                        customDate = date
                    },
                    colors = CalendarDefaults.colors {
                        copy(
                            rightIconTint = chart4,
                            leftIconTint = chart4,
                            monthText = chart4,
                            yearText = chart4,
                            weekDaysText = chart4,
                            dateCellBgStyle = dateCellBgStyle.copy(
                                selectedDate = chart3,
                                todayUnselectedBg = chart3.copy(alpha = 0.1f),
                            ),
                            dateCellTextStyle = dateCellTextStyle.copy(
                                todayUnselected = chart3,
                                currentMonthUnselected = chart3,
                                previousAndNextDateMonth = chart3.copy(alpha = 0.3f),
                            )
                        )
                    }
                )
            }
        }
    }
}