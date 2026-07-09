package com.komoui.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.komoui.themes.styles

// Shared clickable helpers for KomoUI's hand-rolled interactive components.
// Every one provides three things the raw foundation modifiers left out across the library:
//   1. ripple press/hover indication (was `indication = null` everywhere),
//   2. a visible keyboard-focus ring (styles.ring), which nothing had, and
//   3. a role + optional stateDescription so screen readers announce the control.
// See feedback.md §2.1 / issue shadcn-ui-kmp-mjl.1.

/**
 * Draws a 2.dp focus ring in [color] following [shape] when [focused]. Inset stroke, so it
 * never changes layout size. Apply as the outermost visual so it paints over any background.
 */
internal fun Modifier.komoFocusRing(focused: Boolean, color: Color, shape: Shape): Modifier =
    if (focused) this.border(2.dp, color, shape) else this

private fun Modifier.komoStateDescription(desc: String?): Modifier =
    if (desc != null) this.semantics { stateDescription = desc } else this

/**
 * Clickable with ripple, keyboard-focus ring, and a semantics [role]/[stateDescription].
 *
 * @param shape Shape the focus ring follows — pass the same shape the element is clipped to.
 */
fun Modifier.komoClickable(
    onClick: () -> Unit,
    enabled: Boolean = true,
    role: Role? = Role.Button,
    shape: Shape = RectangleShape,
    stateDescription: String? = null,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    komoFocusRing(focused, MaterialTheme.styles.ring, shape)
        .clickable(
            interactionSource = source,
            indication = ripple(),
            enabled = enabled,
            role = role,
            onClickLabel = onClickLabel,
            onClick = onClick,
        )
        .komoStateDescription(stateDescription)
}

/**
 * Toggleable (checkbox/switch) with ripple, keyboard-focus ring, and role/state semantics.
 */
fun Modifier.komoToggleable(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    role: Role = Role.Checkbox,
    shape: Shape = RectangleShape,
    stateDescription: String? = null,
    interactionSource: MutableInteractionSource? = null,
): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    komoFocusRing(focused, MaterialTheme.styles.ring, shape)
        .toggleable(
            value = value,
            interactionSource = source,
            indication = ripple(),
            enabled = enabled,
            role = role,
            onValueChange = onValueChange,
        )
        .komoStateDescription(stateDescription)
}

/**
 * Selectable (tab/day/radio) with ripple, keyboard-focus ring, and role/state semantics.
 */
fun Modifier.komoSelectable(
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
    role: Role = Role.Tab,
    shape: Shape = RectangleShape,
    stateDescription: String? = null,
    interactionSource: MutableInteractionSource? = null,
): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    komoFocusRing(focused, MaterialTheme.styles.ring, shape)
        .selectable(
            selected = selected,
            interactionSource = source,
            indication = ripple(),
            enabled = enabled,
            role = role,
            onClick = onClick,
        )
        .komoStateDescription(stateDescription)
}
