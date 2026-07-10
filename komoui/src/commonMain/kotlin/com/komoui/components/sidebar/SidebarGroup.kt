package com.komoui.components.sidebar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.components.Button
import com.komoui.components.ButtonSize
import com.komoui.components.ButtonVariant
import com.komoui.themes.komoTypography
import com.komoui.themes.styles

/**
 * Container for a logical group of sidebar items (label + optional action + content).
 */
@Composable
fun SidebarGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        content = content,
    )
}

/**
 * Slot-based group label. Hidden when collapsed to icon (matches React behavior).
 * Place inside [SidebarGroup].
 */
@Composable
fun SidebarGroupLabel(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val state = LocalSidebarState.current
    if (state.isCollapsedIcon) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** Text-overload of [SidebarGroupLabel]. */
@Composable
fun SidebarGroupLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val state = LocalSidebarState.current
    if (state.isCollapsedIcon) return

    Text(
        text = text,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.komoTypography.labelMedium,
        color = MaterialTheme.styles.sidebarForeground.copy(alpha = 0.7f),
    )
}

/**
 * Trailing action button for a group (e.g. "+ add"). Hidden when collapsed to icon.
 * Place inside [SidebarGroup], typically right after [SidebarGroupLabel].
 */
@Composable
fun SidebarGroupAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val state = LocalSidebarState.current
    if (state.isCollapsedIcon) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        // No hard .size(20.dp): it clamped the button below the 48.dp touch target Button now
        // reserves. IconSm keeps the compact visual.
        Button(
            onClick = onClick,
            size = ButtonSize.IconSm,
            variant = ButtonVariant.Ghost,
            content = { content() },
        )
    }
}

/** Plain wrapper for the items of a [SidebarGroup]. */
@Composable
fun SidebarGroupContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        content = content,
    )
}
