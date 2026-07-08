package com.komoui.components.sidebar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Which screen edge the sidebar is anchored to. */
enum class SidebarSide { Left, Right }

/**
 * Visual chrome of the sidebar root.
 *
 * - [Sidebar]: the standard full-bleed sidebar.
 * - [Floating]: padded outer container, bordered rounded card.
 * - [Inset]: sidebar extends to the edge; main content sits in a rounded inset card.
 */
enum class SidebarVariant { Sidebar, Floating, Inset }

/** How the sidebar collapses on desktop. */
enum class SidebarCollapsible {
    /** Slides off-screen when collapsed. */
    Offcanvas,
    /** Collapses to an icon-only rail. */
    Icon,
    /** Always visible, cannot be toggled. */
    None,
}

/** Convenience snapshot of the open/closed state. */
enum class SidebarStateValue { Expanded, Collapsed }

/**
 * Tunable defaults for the sidebar. Override per-call on [SidebarProvider] or wrap to set globally.
 */
object SidebarDefaults {
    /** Desktop expanded width (~16rem). */
    val Width: Dp = 256.dp
    /** Mobile drawer width (~18rem). */
    val WidthMobile: Dp = 288.dp
    /** Collapsed icon-rail width (~3rem). */
    val WidthIcon: Dp = 48.dp
    /** Below this width the sidebar uses a modal drawer. */
    val MobileBreakpoint: Dp = 768.dp
    /** Width / inset transition spec. */
    val AnimationSpec: AnimationSpec<Dp> = tween(durationMillis = 200)
}

/**
 * State carrier for the sidebar. Provided through [LocalSidebarState] by [SidebarProvider].
 *
 * Read this inside any child composable (e.g. [SidebarTrigger]) to react to open/collapse
 * state, viewport, or variant. Do not construct directly.
 */
@Stable
class SidebarState internal constructor(
    val isMobile: Boolean,
    val side: SidebarSide,
    val variant: SidebarVariant,
    val collapsible: SidebarCollapsible,
    val isOpen: Boolean,
    val isOpenMobile: Boolean,
    val width: Dp,
    val widthMobile: Dp,
    val widthIcon: Dp,
    internal val animationSpec: AnimationSpec<Dp>,
    private val onOpenChange: (Boolean) -> Unit,
    private val onOpenMobileChange: (Boolean) -> Unit,
) {
    /** Coarse expanded/collapsed marker. Mirrors React's `data-state`. */
    val state: SidebarStateValue
        get() = if ((isMobile && isOpenMobile) || (!isMobile && isOpen)) {
            SidebarStateValue.Expanded
        } else {
            SidebarStateValue.Collapsed
        }

    /**
     * True when the sidebar is showing only icons (closed on desktop with `collapsible = Icon`).
     * Labels, badges, sub-menus, group labels, and group actions should hide; menu tooltips
     * should activate.
     */
    val isCollapsedIcon: Boolean
        get() = !isOpen && !isMobile && collapsible == SidebarCollapsible.Icon

    /** Toggle open/closed for the active viewport. No-op when collapsible is [SidebarCollapsible.None]. */
    fun toggleSidebar() {
        if (collapsible == SidebarCollapsible.None) return
        if (isMobile) onOpenMobileChange(!isOpenMobile) else onOpenChange(!isOpen)
    }

    /** Force-close the active viewport's sidebar. */
    fun closeSidebar() {
        if (isMobile) onOpenMobileChange(false) else onOpenChange(false)
    }

    /** Force-open the active viewport's sidebar. */
    fun openSidebar() {
        if (collapsible == SidebarCollapsible.None) return
        if (isMobile) onOpenMobileChange(true) else onOpenChange(true)
    }

    internal fun setOpen(open: Boolean) = onOpenChange(open)
    internal fun setOpenMobile(open: Boolean) = onOpenMobileChange(open)
}

/**
 * Provides the active [SidebarState]. Throws when used outside a [SidebarProvider].
 *
 * Read-tracking (`compositionLocalOf`, not `staticCompositionLocalOf`): a new state on
 * toggle recomposes only the composables that actually read it, not the whole subtree.
 */
val LocalSidebarState = compositionLocalOf<SidebarState> {
    error("SidebarState not provided. Wrap your content in SidebarProvider.")
}

/**
 * Internal slot registry. [Sidebar] and [SidebarInset] write their composable content here
 * during composition; [SidebarProvider] reads it back and places each slot in the proper
 * layout position (Row on desktop, ModalNavigationDrawer on mobile).
 */
internal class SidebarSlots {
    var sidebar: (@Composable () -> Unit)? = null
    var inset: (@Composable () -> Unit)? = null
    /** Set by [SidebarRail] to opt the sidebar shell into rendering an outer-edge rail. */
    var railEnabled: Boolean = false
}

internal val LocalSidebarSlots = compositionLocalOf<SidebarSlots?> { null }
