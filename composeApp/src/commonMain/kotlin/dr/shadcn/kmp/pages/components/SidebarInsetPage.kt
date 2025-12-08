package dr.shadcn.kmp.pages.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.navigation.compose.rememberNavController
import com.shadcn.ui.components.sidebar.LocalSidebarState
import com.shadcn.ui.components.sidebar.Sidebar
import com.shadcn.ui.components.sidebar.SidebarContent
import com.shadcn.ui.components.sidebar.SidebarFooter
import com.shadcn.ui.components.sidebar.SidebarGroup
import com.shadcn.ui.components.sidebar.SidebarGroupContent
import com.shadcn.ui.components.sidebar.SidebarHeader
import com.shadcn.ui.components.sidebar.SidebarInset
import com.shadcn.ui.components.sidebar.SidebarLabel
import com.shadcn.ui.components.sidebar.SidebarMenu
import com.shadcn.ui.components.sidebar.SidebarMenuButton
import com.shadcn.ui.components.sidebar.SidebarProvider
import com.shadcn.ui.components.sidebar.SidebarTrigger
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.Content
import dr.shadcn.kmp.SidebarRoute
import dr.shadcn.kmp.navigation.SidebarNavigation

@Composable
fun SidebarInsetPage() {
    val menus = listOf(
        Content("Dashboard", SidebarRoute.Dashboard.path),
        Content("Projects", SidebarRoute.Project.path),
        Content("Tasks", SidebarRoute.Task.path)
    )
    var selectedItem by remember { mutableStateOf("Dashboard") }
    val sidebarNav = rememberNavController()
    // Define sidebar content once to avoid duplication
    val sidebarContent: @Composable () -> Unit = {
        SidebarContent {
            SidebarHeader {
                Text(
                    text = "My App",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.styles.sidebarForeground
                )
            }

            SidebarGroup {
                SidebarLabel("Navigation")
                SidebarGroupContent {
                    SidebarMenu {
                        menus.forEach { item ->
                            SidebarMenuButton(
                                text = item.title,
                                onClick = {
                                    selectedItem = item.title
                                    sidebarNav.navigate(item.route)
                                },
                                isActive = selectedItem == item.title
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SidebarFooter {
                Text(
                    text = "v1.0.0",
                    fontSize = 12.sp,
                    color = MaterialTheme.styles.mutedForeground
                )
            }
        }
    }

    SidebarProvider(defaultOpen = false) {
        val sidebarState = LocalSidebarState.current

        // Root layout
        Row(modifier = Modifier.fillMaxSize()) {
            // Desktop: Place sidebar manually when open
            if (!sidebarState.isMobile) {
                AnimatedVisibility(
                    visible = sidebarState.isOpen,
                ) {
                    Sidebar {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(280.dp)
                                .background(MaterialTheme.styles.sidebar)
                        ) {
                            sidebarContent()
                        }
                    }
                }
            }

            // Main content with SidebarInset
            SidebarInset(
                modifier = Modifier.weight(1f), // Takes remaining space
                sidebarContent = sidebarContent,
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.styles.background)
                            .padding(16.dp)
                    ) {
                        // Header with trigger
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
            )
        }
    }
}