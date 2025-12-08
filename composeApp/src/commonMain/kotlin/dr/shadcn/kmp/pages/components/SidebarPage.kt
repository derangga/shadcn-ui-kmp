package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shadcn.ui.components.sidebar.SidebarContent
import com.shadcn.ui.components.sidebar.SidebarFooter
import com.shadcn.ui.components.sidebar.SidebarGroup
import com.shadcn.ui.components.sidebar.SidebarGroupContent
import com.shadcn.ui.components.sidebar.SidebarHeader
import com.shadcn.ui.components.sidebar.SidebarLabel
import com.shadcn.ui.components.sidebar.SidebarLayout
import com.shadcn.ui.components.sidebar.SidebarMenu
import com.shadcn.ui.components.sidebar.SidebarMenuButton
import com.shadcn.ui.components.sidebar.SidebarProvider
import com.shadcn.ui.components.sidebar.SidebarTrigger
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.Content
import dr.shadcn.kmp.SidebarRoute
import dr.shadcn.kmp.navigation.SidebarNavigation

@Composable
fun AppSidebar(sidebarNav: NavHostController, selectedMenu: String, onMenuClick: (String) -> Unit) {
    val menus = listOf(
        Content("Dashboard", SidebarRoute.Dashboard.path),
        Content("Projects", SidebarRoute.Project.path),
        Content("Tasks", SidebarRoute.Task.path),
    )
    SidebarContent {
        SidebarGroup {
            SidebarLabel("Navigation")
            SidebarGroupContent {
                SidebarMenu {
                    menus.forEach { item ->
                        SidebarMenuButton(
                            text = item.title,
                            onClick = {
                                onMenuClick(item.title)
                                sidebarNav.navigate(item.route)
                            },
                            isActive = selectedMenu == item.title
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SidebarLayoutPage() {
    var selectedItem by remember { mutableStateOf("Dashboard") }
    val sidebarNav = rememberNavController()
    SidebarProvider(defaultOpen = true) { // Start with sidebar open on desktop
        SidebarLayout(
            sidebarHeader = {
                SidebarHeader {
                    Text(
                        text = "My App",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp),
                        color = MaterialTheme.styles.sidebarForeground
                    )
                }
            },
            sidebarContent = {
                AppSidebar(sidebarNav, selectedItem) { selectedItem = it }
            },
            sidebarFooter = {
                SidebarFooter {
                    Text(
                        text = "© 2025 Shadcn Compose",
                        fontSize = 12.sp,
                        color = MaterialTheme.styles.mutedForeground
                    )
                }
            }
        ) {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.styles.background)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SidebarTrigger()
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = selectedItem,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.styles.foreground
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                SidebarNavigation(sidebarNav)
            }
        }
    }
}
