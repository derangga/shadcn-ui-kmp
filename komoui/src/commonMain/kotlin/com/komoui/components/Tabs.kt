package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.themes.radius
import com.komoui.themes.styles
import com.komoui.utils.komoSelectable

/**
 * @param selectedTabIndex The index of the currently selected tab.
 * @param onTabSelected Callback invoked when a tab is selected.
 * @param tabs List of tab names.
 * @param modifier The modifier to be applied to the tabs container.
 * @param content Composable content for each tab.
 */
@Composable
fun Tabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>,
    modifier: Modifier = Modifier,
    content: @Composable (tabIndex: Int) -> Unit
) {
    Column(modifier = modifier) {
        TabsList(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
            tabs = tabs
        )

        content(selectedTabIndex)
    }
}

/**
 * @param selectedTabIndex The index of the currently selected tab.
 * @param onTabSelected Callback invoked when a tab is selected.
 * @param tabs List of tab names.
 * @param modifier The modifier to be applied to the tabs container.
 */
@Composable
fun TabsList(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>,
    modifier: Modifier = Modifier
) {
    val radius = MaterialTheme.radius
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.styles.muted,
                shape = RoundedCornerShape(radius.md)
            )
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, tab ->
                TabsTrigger(
                    text = tab,
                    isSelected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * @param text The text to display.
 * @param isSelected Whether the tab is currently selected.
 * @param onClick Callback invoked when the tab is clicked.
 * @param modifier The modifier to be applied to the tab.
 */
@Composable
fun TabsTrigger(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) styles.background else styles.muted,
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected)
            styles.foreground
        else
            styles.mutedForeground,
        animationSpec = tween(200),
        label = "textColor"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius.sm))
            .background(backgroundColor)
            .komoSelectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.Tab,
                shape = RoundedCornerShape(radius.sm),
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

/**
 * KomoUI-style TabsContent component
 */
@Composable
fun TabsContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        content()
    }
}
