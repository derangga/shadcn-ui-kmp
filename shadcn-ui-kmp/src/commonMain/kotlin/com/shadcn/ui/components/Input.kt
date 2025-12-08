package com.shadcn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.ShadcnStyles
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

enum class InputVariant {
    Outlined,
    Underlined
}

/**
 * A Jetpack Compose Input component inspired by Shadcn UI.
 * Provides a customizable text input field with Shadcn styling.
 *
 * @param value The current text value of the input field.
 * @param onValueChange Callback invoked when the text in the input field changes.
 * @param modifier The modifier to be applied to the input field.
 * @param placeholder The placeholder text to display when the input is empty.
 * @param enabled Whether the input field is enabled for interaction.
 * @param readOnly Whether the input field is read-only.
 * @param isError Whether the input field is in an error state.
 * @param visualTransformation The visual transformation to be applied to the input field's text.
 * @param interactionSource The [MutableInteractionSource] that will be used to dispatch events when the input field is interacted with.
 * @param leadingIcon Optional composable to display at the start of the input field.
 * @param trailingIcon Optional composable to display at the end of the input field.
 * @param singleLine When true, this text field will not allow multiple lines of text.
 * @param maxLines The maximum number of lines for the input field.
 * @param minLines The minimum number of lines for the input field.
 * @param keyboardOptions Software keyboard options that contain type, capitalization, auto-correct and action.
 * @param keyboardActions When the input service emits an IME action, the corresponding callback is called.
 * @param variant The visual style of the input field (Outlined or Underlined).
 * @param supportingText Optional composable to display as a supporting text below the input field.
 * @param colors [InputStyle] that will be used to resolve the colors used for this input in
 */
@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    variant: InputVariant = InputVariant.Outlined,
    colors: InputStyle = InputDefaults.colors()
) {
    val themeColors = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val currentInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by currentInteractionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.border.error
            isFocused -> colors.border.focus
            else -> colors.border.default
        },
        animationSpec = tween(durationMillis = 150), label = "borderColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) colors.background else colors.disableBackground,
        animationSpec = tween(durationMillis = 150), label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (enabled) colors.text else colors.disableText,
        animationSpec = tween(durationMillis = 150), label = "textColor"
    )

    val placeholderColor = colors.placeholder
    val borderStyle = when (variant) {
        InputVariant.Outlined -> Modifier.border(1.dp, borderColor, RoundedCornerShape(radius.md))
        InputVariant.Underlined -> Modifier.drawBehind {
            val strokeWidth = 1.dp.toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = borderColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 44.dp)
                .background(backgroundColor, RoundedCornerShape(radius.md))
                .then(borderStyle),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = TextStyle(
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = currentInteractionSource,
            cursorBrush = SolidColor(themeColors.foreground),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    leadingIcon?.let {
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            ProvideTextStyle(value = TextStyle(color = themeColors.mutedForeground)) {
                                it()
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty() && !isFocused) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    color = placeholderColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                        innerTextField()
                    }

                    trailingIcon?.let {
                        Box(modifier = Modifier.padding(start = 8.dp)) {
                            ProvideTextStyle(value = TextStyle(color = themeColors.mutedForeground)) {
                                it()
                            }
                        }
                    }
                }
            }
        )

        // Supporting Text
        supportingText?.let {
            Spacer(modifier = Modifier.height(2.dp))
            ProvideTextStyle(
                value = TextStyle(
                    color = colors.supportingText,
                    fontSize = 12.sp
                )
            ) {
                it()
            }
        }
    }
}

data class InputBorderStyle(
    val default: Color,
    val focus: Color,
    val error: Color,
)
data class InputStyle(
    val background: Color,
    val disableBackground: Color,
    val text: Color,
    val disableText: Color,
    val placeholder: Color,
    val border: InputBorderStyle,
    val supportingText: Color,
)

object InputDefaults {
    private fun colorsFrom(colors: ShadcnStyles): InputStyle {
        return InputStyle(
            background = Color.Unspecified,
            disableBackground = colors.muted,
            text = colors.foreground,
            disableText = colors.mutedForeground,
            placeholder = colors.mutedForeground.copy(alpha = 0.5f),
            border = InputBorderStyle(
                default = colors.input,
                error = colors.destructive,
                focus = colors.ring
            ),
            supportingText = colors.mutedForeground
        )
    }

    @Composable
    fun colors(): InputStyle {
        val styles = MaterialTheme.styles
        return InputStyle(
            background = Color.Unspecified,
            disableBackground = styles.muted,
            text = styles.foreground,
            disableText = styles.mutedForeground,
            placeholder = styles.mutedForeground.copy(alpha = 0.5f),
            border = InputBorderStyle(
                default = styles.input,
                error = styles.destructive,
                focus = styles.ring
            ),
            supportingText = styles.mutedForeground
        )
    }

    @Composable
    fun colors(overrides: InputStyle.() -> InputStyle): InputStyle {
        val styles = MaterialTheme.styles
        return colorsFrom(styles).overrides()
    }
}