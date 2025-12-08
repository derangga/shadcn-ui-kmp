package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Accordion
import com.shadcn.ui.components.AccordionItemData
import dr.shadcn.kmp.components.ContentPageWithTitle

@Composable
fun AccordionPage() {
    val accordionItems = listOf(
        AccordionItemData(
            id = "item-1",
            header = { Text("Is it accessible?") },
            content = { Text("Yes. It adheres to the WAI-ARIA design pattern.") }
        ),
        AccordionItemData(
            id = "item-2",
            header = { Text("Is it styled?") },
            content = { Text("Yes. It comes with default styles that matches the other components' aesthetic.") }
        ),
        AccordionItemData(
            id = "item-3",
            header = { Text("Is it animated?") },
            content = { Text("Yes. It's animated by default, but you can disable it if you prefer.") }
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Accordion",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = "A vertically stacked set of interactive headings that each reveal a section of content.",
            style = MaterialTheme.typography.titleMedium
        )
        ContentPageWithTitle {
            Accordion(items = accordionItems)
        }
    }
}