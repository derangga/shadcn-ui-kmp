package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.DatePicker
import com.shadcn.ui.components.DateSelectionMode
import com.shadcn.ui.kmp.format
import com.shadcn.ui.themes.styles
import com.shadcn.ui.utils.now
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout
import kotlinx.datetime.LocalDate

@Composable
fun DatePickerPage() {
    Layout {
        Text(
            "Date Picker",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "A date picker component with range and presets.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Date picker without pre-filled") {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var selectedDate1 by remember { mutableStateOf<LocalDate?>(null) }
                DatePicker(
                    selectedDate = selectedDate1,
                    onDateSelected = { selectedDate1 = it },
                    modifier = Modifier.width(280.dp),
                    placeholder = "Pick your birthday",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Pick date",
                            tint = MaterialTheme.styles.mutedForeground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )

                Text(
                    text = "Selected Date: ${selectedDate1?.format("MMM dd, yyyy") ?: "None"}",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        ContentPageWithTitle("2. Date picker with pre-filled") {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var selectedDate2 by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
                DatePicker(
                    selectedDate = selectedDate2,
                    onDateSelected = { selectedDate2 = it },
                    modifier = Modifier.width(280.dp),
                    placeholder = "Select an event date",
                    dateSelectionMode = DateSelectionMode.FutureOrToday,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Pick date",
                            tint = MaterialTheme.styles.mutedForeground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )

                Text(
                    text = "Event Date: ${selectedDate2?.format("MMM dd, yyyy") ?: "None"}",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}