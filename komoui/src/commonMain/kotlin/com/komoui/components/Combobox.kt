package com.komoui.components

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.komoui.themes.radius
import com.komoui.themes.komoStrings
import com.komoui.themes.styles
import com.komoui.utils.komoClickable
import kotlin.math.roundToInt

/**
 * A Jetpack Compose Combobox component for KomoUI's Dropdown Menu.
 * Provides a searchable dropdown list for selecting an option, appearing as a popover.
 *
 * @param options The list of string options to display in the combobox.
 * @param selectedOption The currently selected option. Null if no option is selected.
 * @param onOptionSelected Callback invoked when an option is selected. Provides the selected string.
 * @param modifier The modifier to be applied to the combobox container.
 * @param enabled Controls the enabled state of the combobox. When `false`, this combobox will not
 *      be interactable and will appear visually muted.
 * @param placeholder The placeholder text to display when no option is selected.
 * @param dropdownMaxHeight The maximum height of the dropdown list. Defaults to 200.dp.
 */
@Composable
fun ComboBox(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select option...",
    dropdownMaxHeight: Dp = 200.dp
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(selectedOption ?: "") }

    // State to hold the position and width of the input field
    var inputWidthPx by remember { mutableIntStateOf(0) }
    var inputHeightPx by remember { mutableIntStateOf(0) }
    var inputXPositionPx by remember { mutableIntStateOf(0) }
    var inputYPositionPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    // Update searchText when selectedOption changes externally, but only if the dropdown is not expanded
    LaunchedEffect(selectedOption, expanded) {
        if (selectedOption != searchText && !expanded) {
            searchText = selectedOption ?: ""
        }
    }

    val filteredOptions = remember(options, searchText) {
        if (searchText.isBlank()) {
            options
        } else {
            options.filter { it.contains(searchText, ignoreCase = true) }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor by animateColorAsState(
        targetValue = when {
            !enabled -> styles.border
            isFocused || isPressed || expanded -> styles.ring
            else -> styles.border
        },
        animationSpec = tween(150), label = "comboboxBorderColor"
    )

    val textColor = if (enabled) styles.foreground else styles.mutedForeground
    val iconTint = styles.mutedForeground
    val containerBackground = if (enabled) Color.Transparent else styles.muted

    Column(modifier = modifier) {
        // Input field that triggers the dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onGloballyPositioned { coordinates ->
                    inputWidthPx = coordinates.size.width
                    inputHeightPx = coordinates.size.height
                    val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                    inputXPositionPx = position?.x?.roundToInt() ?: 0
                    inputYPositionPx = position?.y?.roundToInt() ?: 0
                }
                .clip(RoundedCornerShape(radius.md))
                .background(containerBackground)
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md))
                .then(
                    if (enabled) {
                        Modifier.komoClickable(
                            onClick = {
                                expanded = !expanded
                                if (expanded) {
                                    searchText = ""
                                } else {
                                    if (selectedOption == null && searchText.isNotBlank()) {
                                        searchText = ""
                                    }
                                }
                            },
                            role = Role.DropdownList,
                            shape = RoundedCornerShape(radius.md),
                            stateDescription = if (expanded) "Expanded" else "Collapsed",
                            interactionSource = interactionSource,
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedOption ?: placeholder,
                    color = if (selectedOption != null) textColor else styles.mutedForeground,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown arrow",
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Dropdown Popup
        if (expanded) {
            Popup(
                offset = IntOffset(inputXPositionPx, inputYPositionPx + inputHeightPx),
                properties = PopupProperties(focusable = true),
                onDismissRequest = {
                    expanded = false
                    if (selectedOption == null) {
                        searchText = ""
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .shadow(1.dp, RoundedCornerShape(radius.lg))
                ) {
                    Column(
                        modifier = Modifier
                            .width(with(density) { inputWidthPx.toDp() })
                            .clip(RoundedCornerShape(radius.lg))
                            .background(styles.popover)
                            .border(1.dp, styles.border, RoundedCornerShape(radius.lg))
                            .padding(8.dp)
                    ) {
                        val searchFocusRequester = remember { FocusRequester() }
                        LaunchedEffect(Unit) { searchFocusRequester.requestFocus() }
                        Input(
                            value = searchText,
                            onValueChange = { searchText = it },
                            variant = InputVariant.Underlined,
                            placeholder = MaterialTheme.komoStrings.searchPlaceholder,
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search") },
                            singleLine = true,
                            focusRequester = searchFocusRequester,
                        )

                        if (filteredOptions.isEmpty()) {
                            Text(
                                text = MaterialTheme.komoStrings.noResultsFound,
                                color = styles.mutedForeground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = dropdownMaxHeight)
                            ) {
                                items(filteredOptions) { option ->
                                    val isSelected = option == selectedOption
                                    val optionBackgroundColor by animateColorAsState(
                                        targetValue = if (isSelected) styles.accent else Color.Transparent,
                                        animationSpec = tween(100), label = "optionBackgroundColor"
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(radius.sm))
                                            .background(optionBackgroundColor)
                                            .clickable {
                                                onOptionSelected(option)
                                                searchText = option
                                                expanded = false
                                            }
                                            .padding(horizontal = 8.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = option,
                                            color = if (isSelected) styles.accentForeground else styles.popoverForeground,
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
}
