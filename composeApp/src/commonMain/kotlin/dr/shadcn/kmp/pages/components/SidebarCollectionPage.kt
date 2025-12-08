package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shadcn.ui.components.Accordion
import com.shadcn.ui.components.AccordionItemData
import com.shadcn.ui.components.Button
import dr.shadcn.kmp.TopLevelRoute
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun SidebarCollectionPage(parentNav: NavHostController) {
    Layout {
        Text(
            "Sidebar",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "A composable, themeable and customizable sidebar component.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Text("1. Sidebar layout")
        Accordion(
            items =
                listOf(
                    AccordionItemData(
                        id = "item-1",
                        header = { Text("Details") },
                        content = {
                            Text("SidebarLayout is a high-level composable designed to manage both the sidebar and the main content layout in a single component. It handles their placement, behavior, and animations based on the device type (mobile or desktop) and the sidebar's state (open or closed)")
                        }
                    )
                )
        )
        Spacer(modifier = Modifier.height(12.dp))
        ContentPageWithTitle {
            Button(
                onClick = { parentNav.navigate(TopLevelRoute.SidebarLayoutGraph.path) }
            ) { Text("Open page sidebar layout") }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("2. Sidebar inset")
        Accordion(
            items =
                listOf(
                    AccordionItemData(
                        id = "item-1",
                        header = { Text("Details") },
                        content = {
                            Text("SidebarInset is a composable focused on managing the main content area, adjusting its layout based on the sidebar's presence. It’s meant to work alongside a separately placed sidebar, giving you more control over the overall layout")
                        }
                    )
                )
        )
        Spacer(modifier = Modifier.height(12.dp))
        ContentPageWithTitle {
            Button(
                onClick = { parentNav.navigate(TopLevelRoute.SidebarInsetGraph.path) }
            ) { Text("Open page sidebar inset") }
        }
    }
}