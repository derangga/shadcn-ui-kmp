package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.shadcn.ui.components.LayoutOrientation
import com.shadcn.ui.components.RadioButtonWithLabel
import com.shadcn.ui.components.RadioGroup
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun RadioButtonPage() {
    Layout {
        Text(
            "Radio Button",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "A set of checkable buttons—known as radio buttons—where no more than one of the buttons can be checked at a time.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        var selectedOption1 by remember { mutableStateOf("") }
        ContentPageWithTitle("1. Vertical radio group") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Select an option (Vertical):",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RadioGroup(
                    selectedValue = selectedOption1,
                    onValueChange = { selectedOption1 = it },
                    orientation = LayoutOrientation.Vertical
                ) {
                    RadioButtonWithLabel(
                        value = "option-a",
                        label = "Option A",
                        selectedValue = selectedOption1,
                        onValueChange = { selectedOption1 = it })
                    RadioButtonWithLabel(
                        value = "option-b",
                        label = "Option B",
                        selectedValue = selectedOption1,
                        onValueChange = { selectedOption1 = it })
                    RadioButtonWithLabel(
                        value = "option-c",
                        label = "Option C",
                        selectedValue = selectedOption1,
                        onValueChange = { selectedOption1 = it })
                    RadioButtonWithLabel(
                        value = "option-d",
                        label = "Option D (disabled)",
                        selectedValue = selectedOption1,
                        onValueChange = { selectedOption1 = it },
                        enabled = false
                    ) // Disabled option
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Selected: $selectedOption1",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        var selectedOption2 by remember { mutableStateOf("radio-1") }
        ContentPageWithTitle("2: Horizontal radio group") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Select a category (Horizontal):",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RadioGroup(
                    selectedValue = selectedOption2,
                    onValueChange = { selectedOption2 = it },
                    orientation = LayoutOrientation.Horizontal
                ) {
                    RadioButtonWithLabel(
                        value = "radio-1",
                        label = "Category 1",
                        selectedValue = selectedOption2,
                        onValueChange = { selectedOption2 = it })
                    RadioButtonWithLabel(
                        value = "radio-2",
                        label = "Category 2",
                        selectedValue = selectedOption2,
                        onValueChange = { selectedOption2 = it })
                    RadioButtonWithLabel(
                        value = "radio-3",
                        label = "Category 3",
                        selectedValue = selectedOption2,
                        onValueChange = { selectedOption2 = it })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Selected: $selectedOption2",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}