package com.komoui.components.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.components.Skeleton
import com.komoui.themes.komoTypography
import com.komoui.themes.radius
import com.komoui.themes.styles
import com.komoui.utils.komoClickable

// ---------------------------------------------------------------------------
// Variant and size enums (mirror React CVA variants).
// ---------------------------------------------------------------------------

enum class SidebarMenuButtonVariant { Default, Outline }
enum class SidebarMenuButtonSize { Default, Small, Large }

private data class MenuButtonMetrics(val height: Dp, val iconSize: Dp)

private fun SidebarMenuButtonSize.metrics(): MenuButtonMetrics = when (this) {
    SidebarMenuButtonSize.Default -> MenuButtonMetrics(height = 32.dp, iconSize = 18.dp)
    SidebarMenuButtonSize.Small -> MenuButtonMetrics(height = 28.dp, iconSize = 16.dp)
    SidebarMenuButtonSize.Large -> MenuButtonMetrics(height = 48.dp, iconSize = 20.dp)
}

// ---------------------------------------------------------------------------
// Menu container, item
// ---------------------------------------------------------------------------

/** Vertical container for sidebar menu items. */
@Composable
fun SidebarMenu(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (state.isCollapsedIcon) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        content = content,
    )
}

/**
 * Wrapper for a single menu row. Use as a parent when you need to overlay a
 * [SidebarMenuAction] or [SidebarMenuBadge] on top of a [SidebarMenuButton].
 */
@Composable
fun SidebarMenuItem(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (state.isCollapsedIcon) Alignment.Center else Alignment.TopStart,
        content = content,
    )
}

// ---------------------------------------------------------------------------
// Menu button — slot + text overloads, with tooltip support.
// ---------------------------------------------------------------------------

/**
 * Primary clickable menu row.
 *
 * In icon mode (`collapsible = Icon` and closed on desktop), only the [icon] renders
 * inside a square hit-area. When [tooltip] is set, hovering / long-pressing the icon
 * shows a tooltip with the label — automatically suppressed when the sidebar is expanded.
 *
 * @param onClick Click handler. Auto-closes the mobile drawer after invocation.
 * @param isActive Marks the row as selected (background + foreground swap).
 * @param variant `Default` = transparent / hover-accent; `Outline` = bordered, host bg.
 * @param size See [SidebarMenuButtonSize].
 * @param tooltip Label shown over the icon when collapsed-to-icon. Pass null to disable.
 * @param icon Optional leading icon slot.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SidebarMenuButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    variant: SidebarMenuButtonVariant = SidebarMenuButtonVariant.Default,
    size: SidebarMenuButtonSize = SidebarMenuButtonSize.Default,
    tooltip: String? = null,
    icon: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    val styles = MaterialTheme.styles
    val metrics = size.metrics()
    val shape = RoundedCornerShape(MaterialTheme.radius.md)

    val activeBackground = if (isActive) styles.sidebarAccent else Color.Unspecified
    val borderModifier = if (variant == SidebarMenuButtonVariant.Outline) {
        Modifier.border(width = 1.dp, color = styles.sidebarBorder, shape = shape)
    } else {
        Modifier
    }
    val outlineBackground = if (variant == SidebarMenuButtonVariant.Outline) styles.background else activeBackground

    val clickHandler: () -> Unit = {
        onClick()
        if (state.isMobile) state.closeSidebar()
    }

    val button: @Composable () -> Unit = {
        if (state.isCollapsedIcon) {
            Box(
                modifier = modifier
                    .size(metrics.height)
                    .clip(shape)
                    .background(outlineBackground)
                    .then(borderModifier)
                    .komoClickable(
                        onClick = clickHandler,
                        role = Role.Button,
                        shape = shape,
                        stateDescription = if (isActive) "Selected" else null,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                icon?.invoke()
            }
        } else {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = metrics.height)
                    .clip(shape)
                    .background(outlineBackground)
                    .then(borderModifier)
                    .komoClickable(
                        onClick = clickHandler,
                        role = Role.Button,
                        shape = shape,
                        stateDescription = if (isActive) "Selected" else null,
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Don't constrain the icon slot — user content (Avatar, custom icon, etc.) sets its own size.
                // Suggested default for Material `Icon` callers: `modifier = Modifier.size(metrics.iconSize)`.
                if (icon != null) icon()
                content()
            }
        }
    }

    val tooltipState = rememberTooltipState()
    val tooltipText = tooltip
    if (tooltipText != null && state.isCollapsedIcon && !state.isMobile) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = { PlainTooltip { Text(tooltipText) } },
            state = tooltipState,
            content = { button() },
        )
    } else {
        button()
    }
}

/** Convenience overload that renders [text] as the label and defaults the tooltip to the same text. */
@Composable
fun SidebarMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    variant: SidebarMenuButtonVariant = SidebarMenuButtonVariant.Default,
    size: SidebarMenuButtonSize = SidebarMenuButtonSize.Default,
    tooltip: String? = text,
    icon: (@Composable () -> Unit)? = null,
) {
    val styles = MaterialTheme.styles
    val metrics = size.metrics()
    val contentColor = if (isActive) styles.sidebarAccentForeground else styles.sidebarForeground

    SidebarMenuButton(
        onClick = onClick,
        modifier = modifier,
        isActive = isActive,
        variant = variant,
        size = size,
        tooltip = tooltip,
        icon = icon,
    ) {
        Text(
            text = text,
            color = contentColor,
            style = if (size == SidebarMenuButtonSize.Small) MaterialTheme.komoTypography.labelMedium
            else MaterialTheme.komoTypography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
    }
}

// ---------------------------------------------------------------------------
// Menu action / badge / skeleton
// ---------------------------------------------------------------------------

/**
 * Trailing action affordance for a menu row (e.g. a "more" icon). Hidden when
 * collapsed to icon. Place inside a [SidebarMenuItem] alongside a [SidebarMenuButton].
 */
@Composable
fun SidebarMenuAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val state = LocalSidebarState.current
    if (state.isCollapsedIcon) return

    Box(
        modifier = modifier
            .padding(end = 4.dp)
            .komoClickable(
                onClick = onClick,
                role = Role.Button,
                shape = RoundedCornerShape(MaterialTheme.radius.sm),
            )
            .minimumInteractiveComponentSize()
            .size(20.dp)
            .clip(RoundedCornerShape(MaterialTheme.radius.sm)),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

/** Small trailing badge for counters / status. Hidden when collapsed to icon. */
@Composable
fun SidebarMenuBadge(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val state = LocalSidebarState.current
    if (state.isCollapsedIcon) return

    Box(
        modifier = modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

/** Loading placeholder that mimics a menu row. Adapts shape for icon mode. */
@Composable
fun SidebarMenuSkeleton(
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
) {
    val state = LocalSidebarState.current

    if (state.isCollapsedIcon) {
        if (showIcon) {
            Skeleton(
                modifier = modifier.size(20.dp),
                shape = RoundedCornerShape(MaterialTheme.radius.sm),
            )
        }
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showIcon) {
            Skeleton(modifier = Modifier.size(18.dp), shape = CircleShape)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Skeleton(
            modifier = Modifier
                .height(14.dp)
                .fillMaxWidth(),
        )
    }
}

// ---------------------------------------------------------------------------
// Nested submenu
// ---------------------------------------------------------------------------

/**
 * Container for a nested sub-menu. Hidden when collapsed to icon (matches React).
 * Renders with a left border and indentation.
 */
@Composable
fun SidebarMenuSub(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    if (state.isCollapsedIcon) return

    val borderColor = MaterialTheme.styles.sidebarBorder

    Column(
        modifier = modifier
            .padding(start = 14.dp)
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 1.dp.toPx(),
                )
            }
            .padding(start = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        content = content,
    )
}

/** Single sub-menu row wrapper. */
@Composable
fun SidebarMenuSubItem(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth(), content = content)
}

/** Compact button used inside [SidebarMenuSub]. */
@Composable
fun SidebarMenuSubButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    size: SidebarMenuButtonSize = SidebarMenuButtonSize.Default,
    content: @Composable RowScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    if (state.isCollapsedIcon) return

    val styles = MaterialTheme.styles
    val backgroundColor = if (isActive) styles.sidebarAccent else Color.Unspecified
    val height = when (size) {
        SidebarMenuButtonSize.Small -> 24.dp
        SidebarMenuButtonSize.Default -> 28.dp
        SidebarMenuButtonSize.Large -> 32.dp
    }
    val shape = RoundedCornerShape(MaterialTheme.radius.md)

    val clickHandler: () -> Unit = {
        onClick()
        if (state.isMobile) state.closeSidebar()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = height)
            .clip(shape)
            .background(backgroundColor)
            .komoClickable(
                onClick = clickHandler,
                role = Role.Button,
                shape = shape,
                stateDescription = if (isActive) "Selected" else null,
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}
