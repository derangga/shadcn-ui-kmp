package com.komoui.components.sidebar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.komoui.kmp.getScreenWidth
import com.komoui.themes.styles

/**
 * Root sidebar state provider.
 *
 * Compose children with [Sidebar] and [SidebarInset] as siblings — they register their
 * content into provider-owned slots that the provider then arranges as a Row (desktop)
 * or a `ModalNavigationDrawer` (mobile, viewport < [mobileBreakpoint]).
 *
 * Both controlled (`open` + `onOpenChange`) and uncontrolled (`defaultOpen`) modes are
 * supported. Mobile open state is always uncontrolled and session-scoped.
 *
 * ```
 * SidebarProvider(side = Left, variant = Inset, collapsible = Icon) {
 *     Sidebar {
 *         SidebarHeader { ... }
 *         SidebarContent { ... }
 *         SidebarFooter { ... }
 *     }
 *     SidebarInset { AppContent() }
 * }
 * ```
 *
 * @param defaultOpen Initial open state on desktop in uncontrolled mode. Ignored when [open] is non-null.
 * @param open Controlled open state. Pass non-null together with [onOpenChange] to drive the sidebar externally.
 * @param onOpenChange Called when desktop open state changes in controlled mode.
 * @param side Anchor edge.
 * @param variant Visual chrome — standard / floating / inset.
 * @param collapsible Desktop collapse behavior.
 * @param width Desktop expanded width.
 * @param widthMobile Mobile drawer width.
 * @param widthIcon Collapsed icon-rail width.
 * @param mobileBreakpoint Viewport width below which the mobile drawer is used.
 * @param animationSpec Spec used for width / open-close transitions on desktop.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SidebarProvider(
    modifier: Modifier = Modifier,
    defaultOpen: Boolean = true,
    open: Boolean? = null,
    onOpenChange: ((Boolean) -> Unit)? = null,
    side: SidebarSide = SidebarSide.Left,
    variant: SidebarVariant = SidebarVariant.Sidebar,
    collapsible: SidebarCollapsible = SidebarCollapsible.Offcanvas,
    width: Dp = SidebarDefaults.Width,
    widthMobile: Dp = SidebarDefaults.WidthMobile,
    widthIcon: Dp = SidebarDefaults.WidthIcon,
    mobileBreakpoint: Dp = SidebarDefaults.MobileBreakpoint,
    animationSpec: AnimationSpec<Dp> = SidebarDefaults.AnimationSpec,
    content: @Composable () -> Unit,
) {
    val screenWidth = getScreenWidth()
    val isMobile = screenWidth < mobileBreakpoint

    val controlled = open != null
    var internalOpen by rememberSaveable { mutableStateOf(defaultOpen) }
    val effectiveOpen = open ?: internalOpen

    var openMobile by rememberSaveable { mutableStateOf(false) }

    // When the viewport shifts away from mobile, drop the mobile drawer open state.
    LaunchedEffect(isMobile) {
        if (!isMobile) openMobile = false
    }

    // Kept out of the remember keys so an inline (non-remembered) callback doesn't rebuild
    // SidebarState on every recomposition; the state's captured lambda reads the latest.
    val onOpenChangeState = rememberUpdatedState(onOpenChange)
    val state = remember(
        isMobile, side, variant, collapsible,
        effectiveOpen, openMobile,
        width, widthMobile, widthIcon, animationSpec,
        controlled,
    ) {
        SidebarState(
            isMobile = isMobile,
            side = side,
            variant = variant,
            collapsible = collapsible,
            isOpen = effectiveOpen,
            isOpenMobile = openMobile,
            width = width,
            widthMobile = widthMobile,
            widthIcon = widthIcon,
            animationSpec = animationSpec,
            onOpenChange = { next ->
                if (controlled) onOpenChangeState.value?.invoke(next) else internalOpen = next
            },
            onOpenMobileChange = { next -> openMobile = next },
        )
    }

    val slots = remember { SidebarSlots() }

    val wrapperBackground = if (variant == SidebarVariant.Inset) {
        MaterialTheme.styles.sidebar
    } else {
        Color.Unspecified
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(if (wrapperBackground != Color.Unspecified) Modifier.background(wrapperBackground) else Modifier),
    ) {
        CompositionLocalProvider(
            LocalSidebarState provides state,
            LocalSidebarSlots provides slots,
        ) {
            slots.sidebar = null
            slots.inset = null
            // Children (Sidebar, SidebarInset) register their content into `slots` and emit nothing.
            content()
            // Render the captured slots in the layout appropriate for the viewport.
            SidebarLayoutImpl(state, slots)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SidebarLayoutImpl(state: SidebarState, slots: SidebarSlots) {
    val sidebarSlot = slots.sidebar
    val insetSlot = slots.inset

    if (state.isMobile) {
        val drawerState = rememberDrawerState(
            initialValue = if (state.isOpenMobile) DrawerValue.Open else DrawerValue.Closed,
        )

        // External -> drawer: react to programmatic open/close (e.g. SidebarTrigger).
        LaunchedEffect(state.isOpenMobile) {
            if (state.isOpenMobile && !drawerState.isOpen) drawerState.open()
            if (!state.isOpenMobile && drawerState.isOpen) drawerState.close()
        }
        // Drawer -> external: reflect user gestures (swipe, scrim tap) back into our state.
        LaunchedEffect(drawerState.currentValue) {
            val drawerOpen = drawerState.isOpen
            if (drawerOpen != state.isOpenMobile) state.setOpenMobile(drawerOpen)
        }

        val flipDirection = state.side == SidebarSide.Right
        val hostDirection = LocalLayoutDirection.current
        val outerDirection = if (flipDirection) LayoutDirection.Rtl else hostDirection

        CompositionLocalProvider(LocalLayoutDirection provides outerDirection) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                gesturesEnabled = state.isOpenMobile,
                drawerContent = {
                    // Restore the host's direction inside drawer content so text doesn't mirror.
                    CompositionLocalProvider(LocalLayoutDirection provides hostDirection) {
                        ModalDrawerSheet(
                            drawerContainerColor = MaterialTheme.styles.sidebar,
                            drawerContentColor = MaterialTheme.styles.sidebarForeground,
                            modifier = Modifier.width(state.widthMobile),
                        ) {
                            sidebarSlot?.invoke()
                        }
                    }
                },
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides hostDirection) {
                    insetSlot?.invoke()
                }
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            // Wrap the inset slot in a Row-scoped Box with weight(1f) so the inset reclaims
            // remaining width — including the case where Offcanvas-closed makes the sidebar
            // slot emit nothing.
            val insetBlock: @Composable RowScope.() -> Unit = {
                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    insetSlot?.invoke()
                }
            }
            when (state.side) {
                SidebarSide.Left -> {
                    sidebarSlot?.invoke()
                    insetBlock()
                }
                SidebarSide.Right -> {
                    insetBlock()
                    sidebarSlot?.invoke()
                }
            }
        }
    }
}
