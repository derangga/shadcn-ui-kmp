package com.shadcn.ui.components.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.shadcn.ui.kmp.getScreenWidth

private const val MOBILE_BREAKPOINT = 768

val LocalSidebarState = compositionLocalOf<SidebarState> {
    error("SidebarProvider not found")
}

/**
 * Provider for sidebar state management
 */
@Composable
fun SidebarProvider(
    defaultOpen: Boolean = false,
    content: @Composable () -> Unit
) {

    val screenWidthDp = getScreenWidth()

    // Determine if we're in mobile mode
    val isMobile = screenWidthDp < MOBILE_BREAKPOINT.dp

    // Sidebar state - respects defaultOpen for both mobile and desktop
    var isOpen by rememberSaveable {
        mutableStateOf(if (isMobile) false else defaultOpen)
    }

    // Auto-close sidebar when switching to mobile mode, but preserve desktop state
    LaunchedEffect(isMobile) {
        if (isMobile) {
            isOpen = false
        }
        // Remove auto-open for desktop to allow hiding
    }

    val sidebarState = remember(isOpen, isMobile) {
        SidebarState(
            isOpen = isOpen,
            isMobile = isMobile,
            toggleSidebar = { isOpen = !isOpen },
            closeSidebar = { isOpen = false }
        )
    }

    CompositionLocalProvider(
        LocalSidebarState provides sidebarState
    ) {
        content()
    }
}