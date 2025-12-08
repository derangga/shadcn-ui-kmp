package com.shadcn.ui.components.sidebar

data class SidebarState(
    val isOpen: Boolean,
    val isMobile: Boolean,
    val toggleSidebar: () -> Unit,
    val closeSidebar: () -> Unit
)