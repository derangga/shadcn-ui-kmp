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
import com.shadcn.ui.components.ComboBox
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun ComboBoxPage() {
    Layout {
        Text(
            "Combobox",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "Autocomplete input and command palette with a list of suggestions.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Basic usage combobox") {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val programmingLanguages = remember {
                    listOf(
                        "JavaScript", "Python", "Java", "C#", "C++", "Ruby", "Swift",
                        "Go", "Kotlin", "PHP", "TypeScript", "Rust", "Dart", "Scala"
                    )
                }

                var selectedLanguage by remember { mutableStateOf<String?>(null) }

                ComboBox(
                    options = programmingLanguages,
                    selectedOption = selectedLanguage,
                    onOptionSelected = {
                        selectedLanguage = it
                    },
                    modifier = Modifier.width(280.dp),
                    placeholder = "Select a language..."
                )

                Text(
                    text = "Selected Language: ${selectedLanguage ?: "None"}",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        ContentPageWithTitle("2. Basic usage combobox") {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var selectedFramework by remember { mutableStateOf<String?>("React") }
                val frameworks = remember {
                    listOf(
                        "React", "Angular", "Vue", "Svelte", "Next.js", "Nuxt.js",
                        "Gatsby", "Flutter", "React Native", "Spring Boot", "Django", "Flask"
                    )
                }

                ComboBox(
                    options = frameworks,
                    selectedOption = selectedFramework,
                    onOptionSelected = {
                        selectedFramework = it
                    },
                    modifier = Modifier.width(280.dp),
                    placeholder = "Select a framework..."
                )

                Text(
                    text = "Selected Framework: ${selectedFramework ?: "None"}",
                    color = MaterialTheme.styles.foreground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}