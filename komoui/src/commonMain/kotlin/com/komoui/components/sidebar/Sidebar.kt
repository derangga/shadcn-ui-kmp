package com.komoui.components.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.components.Button
import com.komoui.components.ButtonSize
import com.komoui.components.ButtonVariant
import com.komoui.components.Input
import com.komoui.components.InputVariant
import com.komoui.themes.drawShadows
import com.komoui.themes.radius
import com.komoui.themes.styles

// ---------------------------------------------------------------------------
// Sidebar root + SidebarInset — slot-registering composables.
// They emit nothing themselves; the SidebarProvider's layout renderer invokes
// the registered slots in the correct position (Row on desktop, drawer on mobile).
// ---------------------------------------------------------------------------

/**
 * The sidebar's visual root. Registers its content into the provider's sidebar slot;
 * placement (Row column on desktop, ModalDrawerSheet on mobile) is decided by
 * [SidebarProvider] based on viewport.
 *
 * Place [SidebarHeader], [SidebarContent], [SidebarFooter], and optionally [SidebarRail]
 * inside.
 */
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val slots = LocalSidebarSlots.current
        ?: error("Sidebar must be used inside SidebarProvider.")
    slots.sidebar = { SidebarShell(modifier = modifier, content = content) }
}

@Composable
private fun SidebarShell(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val slots = LocalSidebarSlots.current!!

    if (state.isMobile) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            content = content,
        )
        return
    }

    // Reset per-shell flags (rail) so this composition's `SidebarRail()` calls take effect.
    slots.railEnabled = false

    val desktopShell: @Composable () -> Unit = {
        when (state.variant) {
            SidebarVariant.Sidebar, SidebarVariant.Inset -> DesktopStandardSidebar(modifier, content)
            SidebarVariant.Floating -> DesktopFloatingSidebar(modifier, content)
        }
    }

    // Offcanvas: wrap in AnimatedVisibility so the sidebar slides in/out and the row
    // animates its measured width back to 0 (letting the inset expand smoothly).
    // For Icon / None, the inner width animation handles the transition.
    if (state.collapsible == SidebarCollapsible.Offcanvas) {
        val fromStart = state.side == SidebarSide.Left
        val durationMs = 220
        AnimatedVisibility(
            visible = state.isOpen,
            enter = slideInHorizontally(animationSpec = tween(durationMs)) {
                if (fromStart) -it else it
            } + expandHorizontally(
                animationSpec = tween(durationMs),
                expandFrom = if (fromStart) Alignment.Start else Alignment.End,
            ),
            exit = slideOutHorizontally(animationSpec = tween(durationMs)) {
                if (fromStart) -it else it
            } + shrinkHorizontally(
                animationSpec = tween(durationMs),
                shrinkTowards = if (fromStart) Alignment.Start else Alignment.End,
            ),
        ) {
            desktopShell()
        }
    } else {
        desktopShell()
    }
}

@Composable
private fun DesktopStandardSidebar(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val slots = LocalSidebarSlots.current!!
    val styles = MaterialTheme.styles

    val targetWidth = if (state.isCollapsedIcon) state.widthIcon else state.width
    val animatedWidth by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = state.animationSpec,
        label = "sidebar-width",
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(animatedWidth)
            .background(styles.sidebar),
    ) {
        Column(
            // BG (above) extends to the screen edges so the system bars tint with the sidebar
            // color; the actual content sits inside the safe area.
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = if (state.isCollapsedIcon) Alignment.CenterHorizontally else Alignment.Start,
            content = content,
        )
        // After `content()` composes, slots.railEnabled reflects whether the user called SidebarRail().
        if (slots.railEnabled) {
            RailOverlay(side = state.side)
        }
    }
}

@Composable
private fun DesktopFloatingSidebar(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val slots = LocalSidebarSlots.current!!
    val styles = MaterialTheme.styles

    // Floating-in-icon adds a little for outer padding + border (matches React's calc(width-icon + 1rem + 2px)).
    val targetWidth = if (state.isCollapsedIcon) state.widthIcon + 18.dp else state.width
    val animatedWidth by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = state.animationSpec,
        label = "sidebar-floating-width",
    )
    val shadow = MaterialTheme.styles.shadow()
    val shape = RoundedCornerShape(MaterialTheme.radius.lg)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(animatedWidth)
            .background(MaterialTheme.styles.background)
            // Floating variant: the system bars sit outside the floating card.
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(vertical = 0.dp, horizontal = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .drawShadows(MaterialTheme.radius.xl, shadow)
                .fillMaxSize()
                .clip(shape)
                .background(color = styles.sidebar, shape = shape)
                .border(
                    width = 1.dp,
                    color = styles.sidebarBorder,
                    shape = shape,
                ),
            horizontalAlignment = if (state.isCollapsedIcon) Alignment.CenterHorizontally else Alignment.Start,
            content = content,
        )
        if (slots.railEnabled) {
            RailOverlay(side = state.side)
        }
    }
}

@Composable
private fun BoxScope.RailOverlay(side: SidebarSide) {
    val state = LocalSidebarState.current
    val styles = MaterialTheme.styles

    // ponytail: 20.dp hit strip, not the full 48.dp — a 48-wide rail would overlap sidebar
    // content along the whole edge. Widen further only if touch misses are reported.
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(20.dp)
            .align(if (side == SidebarSide.Left) Alignment.CenterEnd else Alignment.CenterStart)
            .clickable { state.toggleSidebar() },
        contentAlignment = if (side == SidebarSide.Left) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(styles.sidebarBorder),
        )
    }
}

/**
 * Main content area sibling of [Sidebar]. Registers its body into the provider's inset slot;
 * the provider places it as the flex-1 row child on desktop or the host content of the
 * `ModalNavigationDrawer` on mobile.
 *
 * When `variant = SidebarVariant.Inset`, the body is wrapped in a margin + rounded + shadowed
 * card (background = `styles.background`) so the sidebar color shows around it.
 */
@Composable
fun SidebarInset(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val slots = LocalSidebarSlots.current
        ?: error("SidebarInset must be used inside SidebarProvider.")
    slots.inset = { SidebarInsetShell(modifier = modifier, content = content) }
}

@Composable
private fun SidebarInsetShell(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val styles = MaterialTheme.styles

    val isMobile = state.isMobile
    val isInset = state.variant == SidebarVariant.Inset && !isMobile
    val insetShape = RoundedCornerShape(MaterialTheme.radius.xl)

    val outerPadding = when {
        !isInset -> Modifier
        state.side == SidebarSide.Left -> {
            // Match React: m-2, ml-0; when collapsed-to-icon, restore ml-2.
            if (state.isCollapsedIcon) {
                Modifier.padding(start = 0.dp, end = 8.dp)
            } else {
                Modifier.padding(horizontal = 8.dp)
            }
        }
        else -> { // Right
            if (state.isCollapsedIcon) {
                Modifier.padding(start = 8.dp, end = 0.dp)
            } else {
                Modifier.padding(horizontal = 8.dp)
            }
        }
    }

    val shadow = MaterialTheme.styles.shadow()
    val cardModifier = if (isInset) {
        Modifier
            .drawShadows(MaterialTheme.radius.xl, shadow)
            .clip(insetShape)
            .background(color = styles.background, shape = insetShape)
            .border(1.dp, styles.border, insetShape)
    } else {
        Modifier.background(styles.background)
    }

    // System-bar handling differs by variant:
    //  - Inset: padding is applied BEFORE the card so the rounded/shadowed card sits inside the
    //    safe area; the provider's `styles.sidebar` wrapper paints behind the bars.
    //  - Sidebar / Floating / mobile: padding is applied AFTER the background so the inset
    //    background still bleeds edge-to-edge while `content()` stays inside the safe area.
    val insetsModifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
    val insetsBeforeCard = if (isInset) insetsModifier else Modifier
    val insetsAfterCard = if (isInset) Modifier else insetsModifier

    Box(
        modifier = (if (isMobile) Modifier.fillMaxSize() else Modifier.fillMaxHeight())
            .then(insetsBeforeCard)
            .then(outerPadding)
            .then(cardModifier)
            .then(insetsAfterCard)
            .then(modifier),
        content = content,
    )
}

// ---------------------------------------------------------------------------
// Trigger and rail
// ---------------------------------------------------------------------------

/**
 * Ghost icon button that toggles the sidebar in the active viewport.
 *
 * @param content Visual override. Defaults to a hamburger icon.
 */
@Composable
fun SidebarTrigger(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {
        Icon(
            Icons.Default.Menu,
            contentDescription = "Toggle Sidebar",
            tint = MaterialTheme.styles.sidebarForeground,
        )
    },
) {
    val state = LocalSidebarState.current
    Button(
        onClick = { state.toggleSidebar() },
        modifier = modifier,
        size = ButtonSize.Icon,
        variant = ButtonVariant.Ghost,
    ) {
        content()
    }
}

/**
 * Marker composable that opts the sidebar into rendering a thin clickable rail on its outer
 * edge. The rail toggles open/closed when tapped. Hidden when `collapsible = Offcanvas`
 * (matches React) and on mobile (the drawer responds to gestures).
 *
 * Emits nothing on its own — place inside [Sidebar]'s content; the actual rail is drawn by
 * the sidebar shell so it can position absolutely on the outer edge.
 */
@Composable
fun SidebarRail(@Suppress("UNUSED_PARAMETER") modifier: Modifier = Modifier) {
    val slots = LocalSidebarSlots.current
        ?: error("SidebarRail must be used inside Sidebar.")
    val state = LocalSidebarState.current
    if (state.isMobile) return
    if (state.collapsible == SidebarCollapsible.Offcanvas) return
    slots.railEnabled = true
}

// ---------------------------------------------------------------------------
// Structural slots — Header / Footer / Separator / Input / Content
// ---------------------------------------------------------------------------

/** Sticky top section of the sidebar. Reduces padding when collapsed to icon. */
@Composable
fun SidebarHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val horizontal = if (state.isCollapsedIcon) 4.dp else 8.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = if (state.isCollapsedIcon) Alignment.CenterHorizontally else Alignment.Start,
        content = content,
    )
}

/**
 * Brand-style header overload that mirrors `SidebarMenuButton`'s text overload: shows the
 * [title] alongside an optional [icon] when expanded, and collapses to icon-only (or hides
 * entirely when no icon is provided) when the sidebar is in icon mode.
 *
 * Use this instead of the slot overload when the header is a simple "logo + product name"
 * pairing — it stays legible at every collapsed state without manual scope handling.
 */
@Composable
fun SidebarHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    val state = LocalSidebarState.current

    if (state.isCollapsedIcon) {
        if (icon == null) return
        SidebarHeader(modifier = modifier) {
            icon()
        }
        return
    }

    SidebarHeader(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (icon != null) icon()
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.styles.sidebarForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** Sticky bottom section of the sidebar. Reduces padding when collapsed to icon. */
@Composable
fun SidebarFooter(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val horizontal = if (state.isCollapsedIcon) 4.dp else 8.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = if (state.isCollapsedIcon) Alignment.CenterHorizontally else Alignment.Start,
        content = content,
    )
}

/**
 * Brand-style footer overload. Mirrors [SidebarHeader]'s text overload: shows the [text]
 * (e.g. a copyright line or username) alongside an optional [icon] when expanded, and
 * collapses to icon-only — or hides entirely when no icon is provided — in icon mode.
 *
 * Text-only footer content disappears in icon mode and
 * avatar + text patterns reduce to just the avatar. For richer footer content (dropdowns,
 * user menus), use the slot overload with [SidebarMenu] / [SidebarMenuButton] inside, which
 * already adapt to icon mode.
 */
@Composable
fun SidebarFooter(
    text: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    val state = LocalSidebarState.current

    if (state.isCollapsedIcon) {
        if (icon == null) return
        SidebarFooter(modifier = modifier) {
            icon()
        }
        return
    }

    SidebarFooter(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (icon != null) icon()
            Text(
                text = text,
                fontSize = 12.sp,
                color = MaterialTheme.styles.mutedForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** Horizontal divider styled with the sidebar border color. */
@Composable
fun SidebarSeparator(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        thickness = 1.dp,
        color = MaterialTheme.styles.sidebarBorder,
    )
}

/**
 * Sidebar-themed text input. Thin wrapper around [Input]. Mirrors React's [SidebarInput]
 * which uses the host background (not the sidebar background) so the field stays legible.
 */
@Composable
fun SidebarInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {
    Input(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder,
        enabled = enabled,
        singleLine = singleLine,
        variant = InputVariant.Outlined,
    )
}

/**
 * Scrollable main content region of the sidebar. Takes the remaining vertical space inside
 * [Sidebar]. Scrolling is disabled when collapsed to icon to avoid scrollbar artifacts on
 * the narrow rail.
 *
 * Must be called inside [Sidebar]'s [ColumnScope] so it can claim `weight(1f)`.
 */
@Composable
fun ColumnScope.SidebarContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val horizontal = if (state.isCollapsedIcon) 4.dp else 8.dp
    val scrollModifier = if (state.isCollapsedIcon) Modifier else Modifier.verticalScroll(rememberScrollState())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f, fill = true)
            .padding(horizontal = horizontal)
            .then(scrollModifier),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = content,
    )
}
