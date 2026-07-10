package com.komoui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.komoui.utils.komoClickable

/**
 * Scope exposing the [CollapsibleTrigger] and [CollapsibleContent] slots inside a
 * [Collapsible]. Carries the current `open` state, `onOpenChange` callback, and
 * `enabled` flag so children do not need to re-declare them.
 */
class CollapsibleScope internal constructor(
    private val open: Boolean,
    private val onOpenChange: (Boolean) -> Unit,
    private val enabled: Boolean
) {
    /**
     * Clickable region that toggles the collapsible.
     *
     * @param modifier Modifier applied to the trigger [Row].
     * @param horizontalArrangement Horizontal arrangement of the trigger's children.
     * @param verticalAlignment Vertical alignment of the trigger's children.
     * @param content Trigger content laid out in a [Row].
     */
    @Composable
    fun CollapsibleTrigger(
        modifier: Modifier = Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.Top,
        content: @Composable RowScope.() -> Unit
    ) {
        Row(
            modifier = modifier.komoClickable(
                onClick = { onOpenChange(!open) },
                enabled = enabled,
                role = Role.Button,
                stateDescription = if (open) "Expanded" else "Collapsed",
            ),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }

    /**
     * Animated region revealed when the collapsible is open.
     *
     * @param modifier Modifier applied to the content [Column].
     * @param content Content laid out in a [Column].
     */
    @Composable
    fun CollapsibleContent(
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        AnimatedVisibility(
            visible = open,
            enter = fadeIn(animationSpec = tween(300)) +
                expandVertically(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)) +
                shrinkVertically(animationSpec = tween(300))
        ) {
            Column(modifier = modifier, content = content)
        }
    }
}

/**
 * A controlled, single-section disclosure primitive. The caller owns the [open]
 * state and is notified of toggle intent via [onOpenChange]. Trigger and content
 * are placed inside the trailing [content] lambda using [CollapsibleScope.CollapsibleTrigger]
 * and [CollapsibleScope.CollapsibleContent].
 *
 * @param open Whether the content is currently expanded.
 * @param onOpenChange Invoked with the requested new open state when the trigger is tapped.
 * @param modifier Modifier applied to the root [Column].
 * @param enabled When false, the trigger does not respond to taps.
 * @param content Slot for [CollapsibleScope.CollapsibleTrigger] and [CollapsibleScope.CollapsibleContent].
 */
@Composable
fun Collapsible(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable CollapsibleScope.() -> Unit
) {
    val scope = remember(open, onOpenChange, enabled) {
        CollapsibleScope(
            open = open,
            onOpenChange = onOpenChange,
            enabled = enabled
        )
    }
    Column(modifier = modifier) {
        scope.content()
    }
}
