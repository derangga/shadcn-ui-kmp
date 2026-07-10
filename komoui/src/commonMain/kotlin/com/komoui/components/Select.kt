package com.komoui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.komoui.themes.radius
import com.komoui.themes.KomoFieldDefaults
import com.komoui.themes.styles
import com.komoui.themes.komoTypography
import com.komoui.utils.komoClickable
import com.komoui.utils.rememberAnchoredPopupPositionProvider

/**
 * A Jetpack Compose Select component for KomoUI.
 * Provides a dropdown list for selecting an option, appearing as a popover.
 *
 * This generic overload allows selecting from a list of any type [T].
 *
 * @param T The type of items in the options list.
 * @param options The list of options to display in the select dropdown.
 * @param selectedOption The currently selected option. Null if no option is selected.
 * @param onOptionSelected Callback invoked when an option is selected. Provides the selected item.
 * @param label A function that converts an option of type [T] to a display string.
 * @param modifier The modifier to be applied to the select container.
 * @param enabled Whether the select is interactive. When false, the component is visually dimmed and cannot be clicked.
 * @param placeholder The placeholder text to display when no option is selected.
 */
@Composable
fun <T> Select(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select option...",
    isError: Boolean = false,
    supportingText: String? = null
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    var expanded by remember { mutableStateOf(false) }

    // Width of the trigger, so the popup can match it. Position/flipping is handled by the provider.
    var inputWidthPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val positionProvider = rememberAnchoredPopupPositionProvider()

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor by animateColorAsState(
        targetValue = when {
            isError -> styles.destructive
            enabled && (isFocused || isPressed || expanded) -> styles.ring
            else -> styles.border
        },
        animationSpec = tween(150), label = "selectBorderColor"
    )

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(200), label = "selectArrowRotation"
    )

    val displayText = selectedOption?.let { label(it) }

    Column(modifier = modifier.alpha(if (enabled) 1f else 0.5f)) {
        // Input field that triggers the dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(KomoFieldDefaults.Height)
                .onGloballyPositioned { coordinates ->
                    inputWidthPx = coordinates.size.width
                }
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md))
                .clip(RoundedCornerShape(radius.md))
                .komoClickable(
                    onClick = { expanded = !expanded },
                    enabled = enabled,
                    role = Role.DropdownList,
                    shape = RoundedCornerShape(radius.md),
                    stateDescription = if (expanded) "Expanded" else "Collapsed",
                    interactionSource = interactionSource,
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Display selected option or placeholder
                Text(
                    text = displayText ?: placeholder,
                    color = if (displayText != null) styles.foreground else styles.mutedForeground,
                    style = MaterialTheme.komoTypography.body,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = styles.mutedForeground,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer { rotationZ = arrowRotation }
                )
            }
        }

        if (supportingText != null) {
            Text(
                text = supportingText,
                color = if (isError) styles.destructive else styles.mutedForeground,
                style = MaterialTheme.komoTypography.label,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Dropdown Popup
        if (expanded) {
            Popup(
                popupPositionProvider = positionProvider,
                properties = PopupProperties(focusable = true), // Make popup focusable to handle outside clicks
                onDismissRequest = { expanded = false }
            ) {
                // Drive visibility from false -> true after composition so the enter transition plays.
                val transitionState = remember { MutableTransitionState(false) }
                transitionState.targetState = true
                AnimatedVisibility(
                    visibleState = transitionState,
                    enter = fadeIn(animationSpec = tween(150)) + expandVertically(animationSpec = tween(150))
                ) {
                    // Dropdown content container
                    Box(
                        modifier = Modifier.shadow(1.dp, RoundedCornerShape(radius.lg))
                    ) {
                        Column(
                            modifier = Modifier
                                .width(with(density) { inputWidthPx.toDp() }) // Match width of the input field
                                .clip(RoundedCornerShape(radius.lg))
                                .background(styles.popover)
                                .border(1.dp, styles.border, RoundedCornerShape(radius.lg))
                                .padding(8.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp)
                            ) {
                                items(options) { option ->
                                    val isSelected = option == selectedOption
                                    val optionBackgroundColor = if (isSelected) styles.accent else styles.popover
                                    val optionTextColor = if (isSelected) styles.accentForeground else styles.popoverForeground

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(radius.sm))
                                            .background(optionBackgroundColor)
                                            .clickable {
                                                onOptionSelected(option)
                                                expanded = false
                                            }
                                            .padding(horizontal = 8.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = label(option),
                                            color = optionTextColor,
                                            style = MaterialTheme.komoTypography.body,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = styles.accentForeground,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * A Jetpack Compose Select component for KomoUI.
 * Provides a dropdown list for selecting a string option, appearing as a popover.
 *
 * This is a convenience overload for [List] of [String] options.
 *
 * @param options The list of string options to display in the select dropdown.
 * @param selectedOption The currently selected option. Null if no option is selected.
 * @param onOptionSelected Callback invoked when an option is selected. Provides the selected string.
 * @param modifier The modifier to be applied to the select container.
 * @param enabled Whether the select is interactive. When false, the component is visually dimmed and cannot be clicked.
 * @param placeholder The placeholder text to display when no option is selected.
 */
@Composable
fun Select(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select option...",
    isError: Boolean = false,
    supportingText: String? = null
) {
    Select(
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = onOptionSelected,
        label = { it },
        modifier = modifier,
        enabled = enabled,
        placeholder = placeholder,
        isError = isError,
        supportingText = supportingText
    )
}
