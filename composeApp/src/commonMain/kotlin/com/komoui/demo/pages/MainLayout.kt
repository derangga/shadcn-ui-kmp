package com.komoui.demo.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.komoui.components.Button
import com.komoui.components.ButtonSize
import com.komoui.components.ButtonVariant
import com.komoui.components.Dialog
import com.komoui.components.DialogTitle
import com.komoui.components.Input
import com.komoui.components.sidebar.Sidebar
import com.komoui.components.sidebar.SidebarCollapsible
import com.komoui.components.sidebar.SidebarContent
import com.komoui.components.sidebar.SidebarFooter
import com.komoui.components.sidebar.SidebarGroup
import com.komoui.components.sidebar.SidebarGroupContent
import com.komoui.components.sidebar.SidebarGroupLabel
import com.komoui.components.sidebar.SidebarHeader
import com.komoui.components.sidebar.SidebarInset
import com.komoui.components.sidebar.SidebarMenu
import com.komoui.components.sidebar.SidebarMenuButton
import com.komoui.components.sidebar.SidebarProvider
import com.komoui.components.sidebar.SidebarTrigger
import com.komoui.themes.radius
import com.komoui.themes.styles
import com.komoui.demo.HomeContent
import com.komoui.demo.MainRoute
import com.komoui.demo.icons.AppIcons
import com.komoui.demo.navigation.ComponentNavigation

@Composable
private fun ColumnScope.AppSidebarContent(
    sidebarNav: NavHostController,
    selectedMenu: String,
    onMenuClick: (String) -> Unit,
) {
    val menus = HomeContent.contents
    SidebarContent {
        SidebarMenuButton(
            text = "Home",
            onClick = {
                onMenuClick("Home")
                sidebarNav.navigate(MainRoute.HomePage.path)
            }
        )
        SidebarGroup {
            SidebarGroupLabel("Components")
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(rootNav: NavHostController, viewModel: MainViewModel, isDark: Boolean) {
    val styles = MaterialTheme.styles

    val childNav = rememberNavController()

    var selectedMenu by remember { mutableStateOf("Dashboard") }
    var showDialog by remember { mutableStateOf(false) }
    val searchTxt by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredMenu by viewModel.searchResults.collectAsStateWithLifecycle()
    SidebarProvider(
        defaultOpen = true,
        collapsible = SidebarCollapsible.Offcanvas,
    ) {
        Sidebar {
            SidebarHeader(
                title = "KomoUI",
                icon = {
                    Box(
                        modifier = Modifier.size(28.dp).background(
                            MaterialTheme.styles.foreground,
                            RoundedCornerShape(MaterialTheme.radius.md),
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            AppIcons.Box,
                            contentDescription = "logo",
                            tint = MaterialTheme.styles.sidebarPrimaryForeground,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
            )
            AppSidebarContent(sidebarNav = childNav, selectedMenu = selectedMenu) { selectedMenu = it }
            SidebarFooter(text = "© 2025 KomoUI")
        }
        SidebarInset {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors()
                            .copy(containerColor = styles.background),
                        title = {
                            Button(
                                variant = ButtonVariant.Outline,
                                color = buttonColor(),
                                onClick = {
                                    showDialog = true
                                },
                            ) {
                                Text(
                                    "Search..",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                        },
                        navigationIcon = {
                            SidebarTrigger()
                        },
                        actions = {
                            Button(
                                variant = ButtonVariant.Ghost,
                                size = ButtonSize.Icon,
                                onClick = {
                                    childNav.navigate(MainRoute.Themes.path)
                                }
                            ) {
                                Icon(
                                    Icons.Default.Palette,
                                    tint = styles.primary,
                                    contentDescription = "themes-selection"
                                )
                            }
                            Button(
                                variant = ButtonVariant.Ghost,
                                size = ButtonSize.Icon,
                                onClick = {
//                                    rootNav.navigate(
//                                        TopLevelRoute.WebviewGraph.pathWithSlug(
//                                            WebViewSlug.Github
//                                        )
//                                    )
                                }
                            ) {
                                Icon(
                                    AppIcons.Github,
                                    tint = styles.primary,
                                    contentDescription = "github"
                                )
                            }
                            Button(
                                variant = ButtonVariant.Ghost,
                                size = ButtonSize.Icon,
                                onClick = {
                                    viewModel.switchTheme(isDark)
                                }
                            ) {
                                Icon(
                                    Icons.Default.DarkMode,
                                    tint = styles.primary,
                                    contentDescription = "themes",
                                )
                            }
                        },
                        modifier = Modifier.shadow(
                            elevation = 2.dp,
                            shape = RectangleShape,
                            clip = false
                        ),
                    )
                },
                containerColor = styles.background,
            ) { ip ->
                ComponentNavigation(
                    parentNav = rootNav,
                    childNav = childNav,
                    prefs = viewModel.prefs,
                    modifier = Modifier.padding(ip)
                )
            }
        }
    }

    Dialog(
        open = showDialog,
        onDismissRequest = { showDialog = false },
        header = { DialogTitle { Text("Search Component") } },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 200.dp,
                        max = 400.dp
                    )
            ) {
                Input(
                    value = searchTxt,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = "Search components",
                    singleLine = true,
                    trailingIcon = {
                        if (searchTxt.isNotBlank()) {
                            Box(
                                modifier = Modifier.clickable {
                                    showDialog = false
                                }
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "close")
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (filteredMenu.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            AppIcons.EmojiDizzy,
                            tint = styles.mutedForeground,
                            contentDescription = "emoji-dizzy",
                            modifier = Modifier.size(46.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Results Found", color = styles.mutedForeground)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredMenu) { content ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                                    .clickable {
                                        showDialog = false
                                        childNav.navigate(content.route)
                                    },
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    AppIcons.Box,
                                    contentDescription = "component",
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(content.title)
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun buttonColor(): ButtonColors {
    val interactionSource = remember { MutableInteractionSource() }
    val styles = MaterialTheme.styles
    val isPressed = interactionSource.collectIsPressedAsState().value
     val containerColor = if (isPressed) styles.secondary.copy(alpha = 0.8f) else styles.secondary
    val animatedContainerColor = animateColorAsState(
        targetValue = containerColor,
        animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
    )
    val animatedContentColor = animateColorAsState(
        targetValue = styles.secondaryForeground,
        animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
    )
    return ButtonDefaults.buttonColors(
        containerColor = animatedContainerColor.value,
        contentColor = animatedContentColor.value,
        disabledContainerColor = styles.secondary.copy(alpha = 0.5f),
        disabledContentColor = styles.secondaryForeground.copy(alpha = 0.5f)
    )
}