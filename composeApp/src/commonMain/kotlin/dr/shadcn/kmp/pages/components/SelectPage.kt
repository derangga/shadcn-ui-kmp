package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.shadcn.ui.components.Select
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun SelectPage() {
    Layout {
        Text(
            "Select",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "Displays a list of options for the user to pick from—triggered by a button.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        val fruits = remember {
            listOf(
                "Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape",
                "Honeydew", "Kiwi", "Lemon", "Mango", "Nectarine", "Orange", "Peach"
            )
        }

        ContentPageWithTitle("1. Basic usage select") {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var selectedFruit by remember { mutableStateOf<String?>(null) }
                Select(
                    options = fruits,
                    selectedOption = selectedFruit,
                    onOptionSelected = {
                        selectedFruit = it
                    },
                    modifier = Modifier.width(280.dp),
                    placeholder = "Select a fruit..."
                )

                Text(
                    text = "Selected Fruit: ${selectedFruit ?: "None"}",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        ContentPageWithTitle("2. Select with pre-filled data") {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var selectedColor by remember { mutableStateOf<String?>("Blue") }
                val colorsList = remember {
                    listOf(
                        "Red", "Green", "Blue", "Yellow", "Purple", "Orange", "Black", "White"
                    )
                }

                Select(
                    options = colorsList,
                    selectedOption = selectedColor,
                    onOptionSelected = {
                        selectedColor = it
                    },
                    modifier = Modifier.width(280.dp),
                    placeholder = "Select a color..."
                )
                Text(
                    text = "Selected Color: ${selectedColor ?: "None"}",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}