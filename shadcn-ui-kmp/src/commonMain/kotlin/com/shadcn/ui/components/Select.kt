package com.shadcn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles
import kotlin.math.roundToInt

/**
 * A Jetpack Compose Select component inspired by Shadcn UI.
 * Provides a dropdown list for selecting an option, appearing as a popover.
 *
 * @param options The list of string options to display in the select dropdown.
 * @param selectedOption The currently selected option. Null if no option is selected.
 * @param onOptionSelected Callback invoked when an option is selected. Provides the selected string.
 * @param modifier The modifier to be applied to the select container.
 * @param placeholder The placeholder text to display when no option is selected.
 */
@Composable
fun Select(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Select option..."
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    var expanded by remember { mutableStateOf(false) }

    // State to hold the position and width of the input field
    var inputWidthPx by remember { mutableIntStateOf(0) }
    var inputHeightPx by remember { mutableIntStateOf(0) }
    var inputXPositionPx by remember { mutableIntStateOf(0) }
    var inputYPositionPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor by animateColorAsState(
        targetValue = if (isFocused || isPressed || expanded) styles.ring else styles.border,
        animationSpec = tween(150), label = "selectBorderColor"
    )

    Column(modifier = modifier) {
        // Input field that triggers the dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onGloballyPositioned { coordinates ->
                    // Get the size and position of the input field
                    inputWidthPx = coordinates.size.width
                    inputHeightPx = coordinates.size.height
                    val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                    inputXPositionPx = position?.x?.roundToInt() ?: 0
                    inputYPositionPx = position?.y?.roundToInt() ?: 0
                }
                .clip(RoundedCornerShape(radius.md))
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    expanded = !expanded
                }
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
                    text = selectedOption ?: placeholder,
                    color = if (selectedOption != null) styles.foreground else styles.mutedForeground,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown arrow",
                    tint = styles.mutedForeground,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Dropdown Popup
        if (expanded) {
            Popup(
                // Position the popup relative to the input field
                offset = IntOffset(inputXPositionPx, inputYPositionPx + inputHeightPx),
                properties = PopupProperties(focusable = true), // Make popup focusable to handle outside clicks
                onDismissRequest = { expanded = false }
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
                                .height(200.dp)
                        ) {
                            items(options) { option ->
                                val isSelected = option == selectedOption
                                val optionBackgroundColor by animateColorAsState(
                                    targetValue = if (isSelected) styles.accent else styles.popover,
                                    animationSpec = tween(100), label = "optionBackgroundColor"
                                )
                                val optionTextColor by animateColorAsState(
                                    targetValue = if (isSelected) styles.accentForeground else styles.popoverForeground,
                                    animationSpec = tween(100), label = "optionTextColor"
                                )

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
                                        text = option,
                                        color = optionTextColor,
                                        fontSize = 14.sp,
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
