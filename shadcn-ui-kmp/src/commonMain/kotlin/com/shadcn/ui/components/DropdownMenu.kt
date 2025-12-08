package com.shadcn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem as ComposeDropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

// --- 3. Dropdown Menu Components ---

/**
 * A Jetpack Compose Dropdown Menu component inspired by Shadcn UI.
 * Displays a popover menu when triggered.
 *
 * @param expanded Boolean state controlling the visibility of the dropdown menu.
 * @param onDismissRequest Callback invoked when the user tries to dismiss the menu.
 * @param trigger The composable content that will act as the trigger for the dropdown menu.
 * @param modifier The modifier to be applied to the dropdown menu's container.
 * @param offset The offset of the dropdown menu from its anchor.
 * @param content The composable content for the dropdown menu items.
 */
@Composable
fun DropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    trigger: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 4.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    Column {
        // The trigger composable (e.g., a button)
        trigger()

        // The DropdownMenu itself
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
                .background(styles.popover, RoundedCornerShape(radius.md))
                .border(1.dp, styles.border, RoundedCornerShape(radius.md))
                .padding(4.dp),
            offset = offset
        ) {
            // Content is provided by the caller, allowing for custom menu items
            content()
        }
    }
}

/**
 * A styled menu item for ShadcnDropdownMenu.
 *
 * @param onClick Callback invoked when the item is clicked.
 * @param modifier The modifier to be applied to the menu item.
 * @param enabled Whether the item is enabled for interaction.
 * @param content The composable content for the menu item.
 */
@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = animateColorAsState(
        targetValue = if (isPressed) styles.accent else styles.background,
        animationSpec = tween(durationMillis = 100), label = "menuItemContainerColor"
    ).value

    val contentColor = animateColorAsState(
        targetValue = if (enabled) styles.foreground else styles.mutedForeground,
        animationSpec = tween(durationMillis = 100), label = "menuItemContentColor"
    ).value

    ComposeDropdownMenuItem(
        text = {
            ProvideTextStyle(
                value = TextStyle(
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        },
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(radius.sm))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        enabled = enabled,
        colors = MenuDefaults.itemColors(
            textColor = contentColor,
            leadingIconColor = contentColor,
            disabledTextColor = styles.mutedForeground,
            trailingIconColor = contentColor,
            disabledLeadingIconColor = styles.mutedForeground,
            disabledTrailingIconColor = styles.mutedForeground,
        ),
        contentPadding = PaddingValues(0.dp), // Remove default padding as we handle it with modifier.padding
        interactionSource = interactionSource
    )
}

/**
 * A styled separator for ShadcnDropdownMenu.
 *
 * @param modifier The modifier to be applied to the separator.
 */
@Composable
fun DropdownMenuSeparator(modifier: Modifier = Modifier) {
    val styles = MaterialTheme.styles
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(styles.muted)
            .padding(vertical = 4.dp)
    )
}