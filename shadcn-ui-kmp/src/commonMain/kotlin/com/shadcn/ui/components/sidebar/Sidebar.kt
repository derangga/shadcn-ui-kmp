package com.shadcn.ui.components.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.ButtonSize
import com.shadcn.ui.components.ButtonVariant
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

/**
 * Main sidebar container
 */
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    sidebarWidth: Dp = 256.dp,
    mobileWidthMobile: Dp = 288.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val sidebarState = LocalSidebarState.current
    val width = if (sidebarState.isMobile) {
        mobileWidthMobile
    } else {
        sidebarWidth
    }
    Column (
        modifier = modifier
            .fillMaxHeight()
            .width(width)
            .background(
                color = MaterialTheme.styles.sidebar,
                shape = if (sidebarState.isMobile) RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp) else RoundedCornerShape(0.dp)
            )
    ) {
        content()
    }
}

/**
 * Sidebar trigger button (hamburger menu)
 */
@Composable
fun SidebarTrigger(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {
        Icon(
            Icons.Default.Menu,
            contentDescription = "Toggle sidebar",
            tint = MaterialTheme.styles.sidebarForeground
        )
    }
) {
    val sidebarState = LocalSidebarState.current

    // Show trigger on mobile always, and on desktop when sidebar is closeable
    Button(
        onClick = sidebarState.toggleSidebar,
        modifier = modifier,
        size = ButtonSize.Icon,
        variant = ButtonVariant.Ghost
    ) {
        content()
    }
}

/**
 * Main content wrapper that adapts to sidebar
 */
@Composable
fun SidebarInset(
    modifier: Modifier = Modifier,
    sidebarContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val sidebarState = LocalSidebarState.current

    if (sidebarState.isMobile) {
        // Mobile: Sidebar overlays the content with a backdrop
        Box(modifier = modifier.fillMaxSize()) {
            // Main content takes full space
            content()

            // Backdrop when sidebar is open
            if (sidebarState.isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { sidebarState.closeSidebar() }
                        .zIndex(1f)
                )
            }

            // Animated sidebar overlay
            AnimatedVisibility(
                visible = sidebarState.isOpen,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                modifier = Modifier.zIndex(2f)
            ) {
                Sidebar {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(280.dp)
                            .background(
                                color = MaterialTheme.styles.sidebar,
                                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                            )
                    ) {
                        sidebarContent()
                    }
                }
            }
        }
    } else {
        // Desktop: Adjust content area based on sidebar presence
        Box(
            modifier = modifier
                .fillMaxSize()
                .then(if (sidebarState.isOpen) Modifier.padding(start = 16.dp) else Modifier)
        ) {
            content()
        }
    }
}

/**
 * Complete sidebar layout wrapper
 */
@Composable
fun SidebarLayout(
    modifier: Modifier = Modifier,
    sidebarHeader: @Composable (() -> Unit)? = null,
    sidebarContent: @Composable (() -> Unit)? = null,
    sidebarFooter: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val sidebarState = LocalSidebarState.current

    if (sidebarState.isMobile) {
        // Mobile layout: Overlay
        Box(modifier = modifier.fillMaxSize()) {
            // Main content
            content()

            // Backdrop
            if (sidebarState.isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { sidebarState.closeSidebar() }
                        .zIndex(1f)
                )
            }

            // Animated sidebar
            AnimatedVisibility(
                visible = sidebarState.isOpen,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                modifier = Modifier.zIndex(2f)
            ) {
                Sidebar {
                    Spacer(modifier = Modifier.height(24.dp))
                    sidebarHeader?.invoke()
                    Box(modifier = Modifier.weight(1f)) {
                        sidebarContent?.invoke()
                    }
                    sidebarFooter?.invoke()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    } else {
        // Desktop layout: Side by side, sidebar can be hidden
        Row(modifier = modifier.fillMaxSize()) {
            // Sidebar - only show when open
            AnimatedVisibility(
                visible = sidebarState.isOpen,
            ) {
                Sidebar {
                    sidebarHeader?.invoke()
                    Box(modifier = Modifier.weight(1f)) {
                        sidebarContent?.invoke()
                    }
                    sidebarFooter?.invoke()
                }
            }

            // Main content - takes remaining space
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                content()
            }
        }
    }
}

/**
 * Sidebar content components
 */
@Composable
fun SidebarContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        content()
    }
}

@Composable
fun SidebarHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column (
        modifier = modifier
            .defaultMinSize(minHeight = 64.dp)
            .fillMaxWidth()
            .padding(top = 24.dp, start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}

@Composable
fun SidebarFooter(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        content()
    }
}

@Composable
fun SidebarGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        content()
    }
}

@Composable
fun SidebarLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.styles.sidebarForeground.copy(alpha = 0.7f)
    )
}

@Composable
fun SidebarGroupContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun SidebarMenu(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun SidebarMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.radius.md))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        content()
    }
}

@Composable
fun SidebarMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false
) {
    val sidebarState = LocalSidebarState.current
    val styles = MaterialTheme.styles
    val backgroundColor = if (isActive) styles.sidebarAccent else Color.Unspecified
    val textColor = if (isActive) styles.sidebarAccentForeground else styles.sidebarForeground

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.radius.md))
            .background(backgroundColor)
            .clickable {
                onClick()
                // Auto-close sidebar on mobile after menu selection
                if (sidebarState.isMobile) {
                    sidebarState.closeSidebar()
                }
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}